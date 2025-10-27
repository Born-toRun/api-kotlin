package kr.kro.btr.adapter.out.persistence.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.QActivityEntity
import kr.kro.btr.domain.entity.QActivityImageMappingEntity
import kr.kro.btr.domain.entity.QActivityParticipationEntity
import kr.kro.btr.domain.entity.QCrewEntity
import kr.kro.btr.domain.entity.QFeedImageMappingEntity
import kr.kro.btr.domain.entity.QObjectStorageEntity
import kr.kro.btr.domain.entity.QUserEntity
import kr.kro.btr.domain.entity.QUserPrivacyEntity
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import org.springframework.stereotype.Component

@Component
class ActivityQuery(
    private val queryFactory: JPAQueryFactory
) {
    fun searchAllByFilter(query: SearchAllActivityQuery): List<ActivityEntity> {
        val activity = QActivityEntity.activityEntity

        // 크루에 소속되지 않은 사용자는 빈 리스트 반환
        if (query.myCrewId == null) {
            return emptyList()
        }

        var whereClause: BooleanExpression = activity.userEntity.crewId.eq(query.myCrewId)
        var optionalWhereClause: BooleanExpression? = null

        if (query.courses?.isNotEmpty() == true) {
            for (course in query.courses) {
                optionalWhereClause = optionalWhereClause?.or(activity.course.contains(course))
                    ?: activity.course.contains(course)
            }

            whereClause = whereClause.and(optionalWhereClause)
        }

        val activityParticipation = QActivityParticipationEntity.activityParticipationEntity
        val activityImageMapping = QActivityImageMappingEntity.activityImageMappingEntity
        val user = QUserEntity.userEntity
        val crew = QCrewEntity.crewEntity
        val userPrivacy = QUserPrivacyEntity.userPrivacyEntity
        val profileImageStorage = QObjectStorageEntity("profileImageStorage")
        val activityImageStorage = QObjectStorageEntity("activityImageStorage")

        return queryFactory
            .selectFrom(activity)
            .leftJoin(activity.activityParticipationEntities, activityParticipation).fetchJoin()
            .leftJoin(activity.activityImageMappingEntities, activityImageMapping).fetchJoin()
            .leftJoin(activityImageMapping.objectStorageEntity, activityImageStorage).fetchJoin()
            .innerJoin(activity.userEntity, user).fetchJoin()
            .innerJoin(user.userPrivacyEntity, userPrivacy).fetchJoin()
            .innerJoin(user.profileImageEntity, profileImageStorage).fetchJoin()
            .innerJoin(user.crewEntity, crew).fetchJoin()
            .where(whereClause)
            .orderBy(activity.id.asc())
            .distinct()
            .fetch()
    }
}
