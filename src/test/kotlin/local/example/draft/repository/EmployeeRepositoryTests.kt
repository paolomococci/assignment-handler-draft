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

package local.example.draft.repository

import local.example.draft.model.Employee
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension::class)
@DataJpaTest
class EmployeeRepositoryTests(
        @Autowired val entityManager: TestEntityManager,
        @Autowired val employeeRepository: EmployeeRepository
) {

    lateinit var employee: Employee

    @Test
    fun `find employee by id`() {
        employee = Employee(firstName = "James", lastName = "Walker", role = "postman")
        entityManager.persist(employee)
        entityManager.flush()
        val employeeId: Long = employee.id
        assertThat(employeeRepository.findById(employeeId).get()).isEqualTo(employee)
    }
}
