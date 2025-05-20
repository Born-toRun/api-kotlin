package kr.kro.btr.base.extension

import kr.kro.btr.support.exception.NotFoundException
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T = findByIdOrNull(id) ?: throw NotFoundException("잘못된 요청입니다.")
