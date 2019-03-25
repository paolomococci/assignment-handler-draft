/**
 *
 * Copyright 2018 paolo mococci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package local.example.draft.assembler

import local.example.draft.controller.AssignmentRestController
import local.example.draft.model.Assignment
import local.example.draft.status.Status
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceAssembler
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class AssignmentResourceAssembler : ResourceAssembler<Assignment, Resource<Assignment>> {
    override fun toResource(assignment: Assignment): Resource<Assignment> {
        val resource = Resource(assignment,
                linkTo(methodOn(AssignmentRestController::class.java).read(assignment.id)).withSelfRel(),
                linkTo(methodOn(AssignmentRestController::class.java).readAll()).withRel("assignments"))
        if (assignment.status === Status.IN_PROGRESS) {
            resource.add(linkTo(methodOn(AssignmentRestController::class.java).cancel(assignment.id)).withRel("cancel"))
            resource.add(linkTo(methodOn(AssignmentRestController::class.java).complete(assignment.id)).withRel("complete"))
        }
        return resource
    }
}
