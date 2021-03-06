/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.billing.servlet;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.GenericHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/user/confirm_signup/*", name = "userconfirmsignupServlet")
public class ConfirmEmailHandler extends GenericHttpServlet {

    @Autowired
    private UserService userServices;

    @Override
    protected void onHandleRequest(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo != null) {
                UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);
                String username = urlTokenizer.getString();
                Integer accountId = urlTokenizer.getInt();
                SimpleUser user = userServices.findUserByUserNameInAccount(username, accountId);

                if (user != null) {
                    user.setStatus(UserStatusConstants.EMAIL_VERIFIED);
                    userServices.updateWithSession(user, username);
                    response.sendRedirect(request.getContextPath() + "/");
                    return;
                } else {
                    PageGeneratorUtil.responeUserNotExistPage(response, username, request.getContextPath() + "/");
                    return;
                }
            } else {
                throw new ResourceNotFoundException();
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
