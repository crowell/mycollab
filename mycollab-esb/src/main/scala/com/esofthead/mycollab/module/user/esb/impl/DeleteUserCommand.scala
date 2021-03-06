/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.esb.impl

import java.util.Arrays

import com.esofthead.mycollab.cache.CleanCacheEvent
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper
import com.esofthead.mycollab.module.project.domain.{ProjectMember, ProjectMemberExample}
import com.esofthead.mycollab.module.project.service.ProjectMemberService
import com.esofthead.mycollab.module.user.esb.DeleteUserEvent
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 1.0
  */
@Component class DeleteUserCommand extends GenericCommand {
  @Autowired private val projectMemberMapper: ProjectMemberMapper = null

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: DeleteUserEvent): Unit = {
    val ex = new ProjectMemberExample
    ex.createCriteria.andStatusIn(Arrays.asList(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
      RegisterStatusConstants.VERIFICATING)).andSaccountidEqualTo(event.accountid).andUsernameEqualTo(event.username)
    val projectMember = new ProjectMember
    projectMember.setStatus(ProjectMemberStatusConstants.INACTIVE)
    projectMemberMapper.updateByExampleSelective(projectMember, ex)
    asyncEventBus.post(new CleanCacheEvent(event.accountid, Array(classOf[ProjectMemberService])))
  }
}