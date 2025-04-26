package kr.kro.btr.adapter.out.persistence.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQuery
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
        val comment = QCommentEntity.commentEntity
        val recommendation = QRecommendationEntity.recommendationEntity
        val whereClause = buildWhereClause(query, feed)

        val feedQuery: JPAQuery<FeedEntity> = queryFactory
            .selectFrom(feed)
            .innerJoin(feed.userEntity, user).fetchJoin()
            .innerJoin(user.crewEntity, crew).fetchJoin()
            .innerJoin(user.userPrivacyEntity, userPrivacy).fetchJoin()
            .leftJoin(feed.feedImageMappingEntities, feedImageMapping).fetchJoin()
            .leftJoin(feedImageMapping.objectStorageEntity, objectStorage).fetchJoin()
            .leftJoin(feed.commentEntities, comment).fetchJoin()
            .leftJoin(feed.recommendationEntities, recommendation).fetchJoin()
            .distinct()
            .where(whereClause)

        val total = feedQuery.fetchCount()

        val contents = feedQuery
            .orderBy(feed.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

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
                feed.userEntity.crewId.eq(query.my?.crewId)
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
