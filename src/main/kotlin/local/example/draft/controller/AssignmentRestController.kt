/**
 *
 * Copyright 2018 paolo mococci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package local.example.draft.controller

import local.example.draft.assembler.AssignmentResourceAssembler
import local.example.draft.exception.AssignmentNotFoundException
import local.example.draft.model.Assignment
import local.example.draft.repository.AssignmentRepository
import local.example.draft.status.Status
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.Resources
import org.springframework.hateoas.VndErrors
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URISyntaxException
import kotlin.streams.toList

@RestController
@RequestMapping("/api")
class AssignmentRestController internal constructor (
        private val assignmentRepository: AssignmentRepository,
        private val assignmentResourceAssembler: AssignmentResourceAssembler
) {

    @PostMapping("/assignments")
    @Throws(URISyntaxException::class)
    internal fun create(@RequestBody assignment: Assignment): ResponseEntity<Resource<Assignment>> {
        assignment.status = Status.IN_PROGRESS
        val temp = assignmentRepository.saveAndFlush(assignment)
        return ResponseEntity.created(linkTo(methodOn(AssignmentRestController::class.java)
                .read(assignment.id))
                .toUri())
                .body(assignmentResourceAssembler.toResource(temp))
    }

    @GetMapping("/assignments/{id}")
    @Throws(URISyntaxException::class)
    internal fun read(@PathVariable id: Long?): Resource<Assignment> {
        return assignmentResourceAssembler.toResource(assignmentRepository.findById(id!!)
                .orElseThrow { AssignmentNotFoundException(id) })
    }

    @GetMapping("/assignments")
    @Throws(URISyntaxException::class)
    internal fun readAll(): Resources<Resource<Assignment>> {
        val assignments = assignmentRepository.findAll().stream()
                .map(assignmentResourceAssembler::toResource).toList()
        return Resources(assignments,
                linkTo(methodOn(AssignmentRestController::class.java)
                        .readAll())
                        .withSelfRel())
    }

    @PutMapping("/assignments/{id}/complete")
    @Throws(URISyntaxException::class)
    internal fun complete(@PathVariable id: Long?): ResponseEntity<ResourceSupport> {
        val assignment = assignmentRepository.findById(id!!).orElseThrow { AssignmentNotFoundException(id) }
        if (assignment.status === Status.IN_PROGRESS) {
            assignment.status = Status.COMPLETED
            return ResponseEntity.ok(assignmentResourceAssembler.toResource(assignmentRepository.saveAndFlush(assignment)))
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(VndErrors.VndError("method not allowed",
                        "can't complete assignment that is in the ${assignment.status} status"))
    }

    @DeleteMapping("/assignments/{id}/cancel")
    @Throws(URISyntaxException::class)
    internal fun cancel(@PathVariable id: Long?): ResponseEntity<ResourceSupport> {
        val assignment = assignmentRepository.findById(id!!).orElseThrow { AssignmentNotFoundException(id) }
        if (assignment.status === Status.IN_PROGRESS) {
            assignment.status = Status.CANCELLED
            return ResponseEntity.ok(assignmentResourceAssembler.toResource(assignmentRepository.saveAndFlush(assignment)))
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(VndErrors.VndError("method not allowed",
                        "can't complete assignment that is in the ${assignment.status} status"))
    }
}
