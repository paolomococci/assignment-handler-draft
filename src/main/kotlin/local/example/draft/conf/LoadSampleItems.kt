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

package local.example.draft.conf

import local.example.draft.model.Assignment
import local.example.draft.model.Employee
import local.example.draft.repository.AssignmentRepository
import local.example.draft.repository.EmployeeRepository
import local.example.draft.status.Status
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadSampleItems {

    @Bean internal fun initEmployeeItems(employeeRepository: EmployeeRepository) = CommandLineRunner {
        employeeRepository.save(Employee(firstName = "John", lastName = "Bender", role = "worker"))
        employeeRepository.save(Employee(firstName = "Paul", lastName = "Smith", role = "worker"))
        employeeRepository.save(Employee(firstName = "Samantha", lastName = "Weaver", role = "worker"))
        employeeRepository.flush()
    }

    @Bean internal fun initAssignmentItems(assignmentRepository: AssignmentRepository) = CommandLineRunner {
        assignmentRepository.save(Assignment(description = "assemble the scaffolds",
                status = Status.IN_PROGRESS))
        assignmentRepository.save((Assignment(description = "mix the paint",
                status = Status.IN_PROGRESS)))
        assignmentRepository.save(Assignment(description = "paint the walls of the room",
                status = Status.IN_PROGRESS))
    }
}
