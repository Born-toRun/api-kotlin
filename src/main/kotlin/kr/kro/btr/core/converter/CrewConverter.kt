package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewResponse
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.Crew
import kr.kro.btr.infrastructure.model.CreateCrewQuery
import org.springframework.stereotype.Component

@Component
class CrewConverter {

    fun map(source: List<Crew>): SearchCrewResponse {
        val crewDetails: List<SearchCrewResponse.CrewDetail> = source.map { crew ->
            SearchCrewResponse.CrewDetail(
                id = crew.id,
                crewName = crew.name,
                contents = crew.contents,
                region = crew.region,
                imageUri = crew.imageUri,
                logoUri = crew.logoUri,
                crewSnsUri = crew.sns
            )
        }

        return  SearchCrewResponse(crewDetails)
    }

    fun map(source: CreateCrewRequest): CreateCrewCommand {
        return CreateCrewCommand(
            name = source.name,
            contents = source.contents,
            sns = source.sns,
            region = source.region,
        )
    }

    fun map(source: List<CrewEntity>): List<Crew> {
        return source.map { crewEntity ->
            Crew(
                id = crewEntity.id,
                name = crewEntity.name,
                contents = crewEntity.contents,
                region = crewEntity.region,
                sns = crewEntity.sns,
                imageUri = crewEntity.imageEntity?.fileUri,
                logoUri = crewEntity.logoEntity?.fileUri
            )
        }
    }

    fun map(source: CreateCrewCommand): CreateCrewQuery {
        return CreateCrewQuery(
            name = source.name,
            contents = source.contents,
            sns = source.sns,
            region = source.region,
        )
    }

    fun map(source: CreateCrewQuery): CrewEntity {
        return CrewEntity(
            name = source.name,
            contents = source.contents,
            sns = source.sns,
            region = source.region,
        )
    }
}