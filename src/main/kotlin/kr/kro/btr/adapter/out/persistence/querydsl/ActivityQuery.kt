package kr.kro.btr.adapter.out.persistence.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.QActivityEntity
import kr.kro.btr.domain.entity.QActivityParticipationEntity
import kr.kro.btr.domain.entity.QCrewEntity
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

        var whereClause: BooleanExpression = activity.userEntity.crewId.eq(query.myCrewId)
        var optionalWhereClause: BooleanExpression? = null

        if (query.courses?.isNotEmpty() == false) {
            for (course in query.courses) {
                optionalWhereClause = optionalWhereClause?.or(activity.course.contains(course))
                    ?: activity.course.contains(course)
            }

            whereClause = whereClause.and(optionalWhereClause)
        }

        val activityParticipation = QActivityParticipationEntity.activityParticipationEntity
        val user = QUserEntity.userEntity
        val crew = QCrewEntity.crewEntity
        val userPrivacy = QUserPrivacyEntity.userPrivacyEntity
        val objectStorage = QObjectStorageEntity.objectStorageEntity

        return queryFactory
            .selectFrom(activity)
            .leftJoin(activity.activityParticipationEntities, activityParticipation).fetchJoin()
            .innerJoin(activity.userEntity, user).fetchJoin()
            .innerJoin(user.userPrivacyEntity, userPrivacy).fetchJoin()
            .innerJoin(user.profileImageEntity, objectStorage).fetchJoin()
            .innerJoin(user.crewEntity, crew).fetchJoin()
            .where(whereClause)
            .orderBy(activity.id.asc())
            .fetch()
    }
}
