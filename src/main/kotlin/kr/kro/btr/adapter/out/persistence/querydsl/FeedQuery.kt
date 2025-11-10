package kr.kro.btr.adapter.out.persistence.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.entity.FeedEntity
import kr.kro.btr.domain.entity.QCommentEntity
import kr.kro.btr.domain.entity.QCrewEntity
import kr.kro.btr.domain.entity.QFeedEntity
import kr.kro.btr.domain.entity.QFeedImageMappingEntity
import kr.kro.btr.domain.entity.QObjectStorageEntity
import kr.kro.btr.domain.entity.QRecommendationEntity
import kr.kro.btr.domain.entity.QUserEntity
import kr.kro.btr.domain.entity.QUserPrivacyEntity
import kr.kro.btr.infrastructure.model.SearchAllFeedQuery
import kr.kro.btr.support.exception.AuthorizationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class FeedQuery(private val queryFactory: JPAQueryFactory) {
    fun searchAllByFilter(query: SearchAllFeedQuery, pageable: Pageable): Page<FeedEntity> {
        val feed = QFeedEntity.feedEntity
        val user = QUserEntity.userEntity
        val crew = QCrewEntity.crewEntity
        val userPrivacy = QUserPrivacyEntity.userPrivacyEntity
        val feedImageMapping = QFeedImageMappingEntity.feedImageMappingEntity
        val objectStorage = QObjectStorageEntity.objectStorageEntity
        val whereClause = buildWhereClause(query, feed)

        val total = if (query.isMyCrew || query.searchedUserIds?.isNotEmpty() == true) {
            queryFactory
                .select(feed.countDistinct())
                .from(feed)
                .innerJoin(feed.userEntity, user)
                .where(whereClause)
                .fetchOne() ?: 0L
        } else {
            queryFactory
                .select(feed.count())
                .from(feed)
                .where(whereClause)
                .fetchOne() ?: 0L
        }

        val contents = queryFactory
            .selectFrom(feed)
            .innerJoin(feed.userEntity, user).fetchJoin()
            .leftJoin(user.userPrivacyEntity, userPrivacy).fetchJoin()
            .leftJoin(user.crewEntity, crew).fetchJoin()
            .where(whereClause)
            .orderBy(feed.id.desc())
            .limit(pageable.pageSize.toLong())
            .fetch()

        if (contents.isNotEmpty()) {
            val feedIds = contents.map { it.id }
            queryFactory
                .selectFrom(feedImageMapping)
                .leftJoin(feedImageMapping.objectStorageEntity, objectStorage).fetchJoin()
                .where(feedImageMapping.feedId.`in`(feedIds))
                .fetch()
        }

        if (contents.isNotEmpty()) {
            val feedIds = contents.map { it.id }

            val recommendation = QRecommendationEntity.recommendationEntity
            queryFactory
                .selectFrom(recommendation)
                .where(
                    recommendation.recommendationType.eq(kr.kro.btr.domain.constant.RecommendationType.FEED)
                        .and(recommendation.contentId.`in`(feedIds))
                )
                .fetch()

            val comment = QCommentEntity.commentEntity
            queryFactory
                .selectFrom(comment)
                .where(comment.feedId.`in`(feedIds))
                .fetch()
        }

        return PageImpl(contents, pageable, total)
    }

    private fun buildWhereClause(query: SearchAllFeedQuery, feed: QFeedEntity): BooleanExpression {
        var whereClause: BooleanExpression = Expressions.TRUE

        query.category?.let {
            whereClause = whereClause.and(feed.category.eq(it))
        }

        if (query.lastFeedId > 0) {
            whereClause = whereClause.and(feed.id.lt(query.lastFeedId))
        }

        whereClause = if (query.isMyCrew) {
            whereClause.and(
                feed.userEntity.crewId.eq(query.my?.crewId ?: throw AuthorizationException("로그인이 필요합니다."))
                    .and(feed.accessLevel.eq(FeedAccessLevel.IN_CREW))
            )
        } else {
            whereClause.and(feed.accessLevel.eq(FeedAccessLevel.ALL))
        }

        query.searchKeyword?.takeIf { it.isNotEmpty() }?.let { keyword ->
            var searchWhereClause = feed.contents.contains(keyword)
            query.searchedUserIds?.takeIf { it.isNotEmpty() }?.let { ids ->
                searchWhereClause = searchWhereClause.or(feed.userId.`in`(ids))
            }
            whereClause = whereClause.and(searchWhereClause)
        }

        return whereClause
    }

    fun increaseViewQty(feedId: Long) {
        val feed = QFeedEntity.feedEntity

        queryFactory.update(feed)
            .set(feed.viewQty, feed.viewQty.add(1))
            .where(feed.id.eq(feedId))
            .execute()
    }
}
