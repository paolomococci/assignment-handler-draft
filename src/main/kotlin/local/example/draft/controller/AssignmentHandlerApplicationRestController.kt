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

import org.springframework.hateoas.ResourceSupport
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.hateoas.mvc.ControllerLinkBuilder.*
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/")
class AssignmentHandlerApplicationRestController {
    @GetMapping
    fun root(): ResourceSupport {
        val resource = ResourceSupport()
        resource.add(linkTo(methodOn(EmployeeRestController::class.java).readAll()).withRel("employees"))
        resource.add(linkTo(methodOn(AssignmentRestController::class.java).readAll()).withRel("assignments"))
        return resource
    }
}
