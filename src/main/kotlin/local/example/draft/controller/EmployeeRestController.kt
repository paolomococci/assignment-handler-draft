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

package local.example.draft.controller

import local.example.draft.assembler.EmployeeResourceAssembler
import local.example.draft.exception.EmployeeNotFoundException
import local.example.draft.model.Employee
import local.example.draft.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URISyntaxException
import kotlin.streams.toList

@RestController
@RequestMapping("/api")
class EmployeeRestController internal constructor (
        private val employeeRepository: EmployeeRepository,
        private val employeeResourceAssembler: EmployeeResourceAssembler
) {

    @PostMapping("/employees")
    @Throws(URISyntaxException::class)
    internal fun create(@RequestBody employee: Employee): ResponseEntity<*> {
        val resource = employeeResourceAssembler.toResource(employeeRepository.saveAndFlush(employee))
        return ResponseEntity.created(URI(resource.id.expand().href)).body(resource)
    }

    @GetMapping("/employees/{id}")
    @Throws(URISyntaxException::class)
    internal fun read(@PathVariable id: Long?): Resource<Employee> {
        val employee = employeeRepository.findById(id!!)
                .orElseThrow { EmployeeNotFoundException(id) }
        return employeeResourceAssembler.toResource(employee)
    }

    @GetMapping("/employees")
    @Throws(URISyntaxException::class)
    internal fun readAll(): Resources<Resource<Employee>> {
        val employees = employeeRepository.findAll().stream()
                .map(employeeResourceAssembler::toResource).toList()
        return Resources(employees,
                linkTo(methodOn(EmployeeRestController::class.java)
                        .readAll()).withSelfRel())
    }

    @PutMapping("/employees/{id}")
    @Throws(URISyntaxException::class)
    internal fun update(@RequestBody employee: Employee, @PathVariable id: Long?): ResponseEntity<*> {
        val updated = employeeRepository.findById(id!!)
                .map { temp ->
                    temp.firstName = employee.firstName
                    temp.lastName = employee.lastName
                    temp.name = employee.name
                    temp.role = employee.role
                    employeeRepository.saveAndFlush(temp)
                }
                .orElseGet { employeeRepository.saveAndFlush(employee) }
        val resource = employeeResourceAssembler.toResource(updated)
        return ResponseEntity.created(URI(resource.id.expand().href)).body(resource)
    }

    @PatchMapping("/employees/{id}")
    @Throws(URISyntaxException::class)
    internal fun partialUpdate(@RequestBody employee: Employee, @PathVariable id: Long?): ResponseEntity<*> {
        val updated = employeeRepository.findById(id!!)
                .map { temp ->
                    if (!employee.firstName.isNullOrBlank()) temp.firstName = employee.firstName
                    if (!employee.lastName.isNullOrBlank()) temp.lastName = employee.lastName
                    if (!employee.name.isNullOrBlank()) temp.name = employee.name
                    if (!employee.role.isNullOrBlank()) temp.role = employee.role
                    employeeRepository.saveAndFlush(temp)
                }
                .orElseGet { employeeRepository.saveAndFlush(employee) }
        val resource = employeeResourceAssembler.toResource(updated)
        return ResponseEntity.created(URI(resource.id.expand().href)).body(resource)
    }

    @DeleteMapping("/employees/{id}")
    @Throws(URISyntaxException::class)
    internal fun delete(@PathVariable id: Long?): ResponseEntity<*> {
        if (id != null) {
            employeeRepository.deleteById(id)
        }
        return ResponseEntity.noContent().build<Any>()
    }
}
