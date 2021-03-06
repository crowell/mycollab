/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.module.crm.domain.CallWithBLOBs;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CallAddPresenter extends AbstractCrmPresenter<CallAddView> {
    private static final long serialVersionUID = 579102306602918760L;

    public CallAddPresenter() {
        super(CallAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<CallWithBLOBs>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final CallWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void onSaveAndNew(final CallWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canWrite(RolePermissionCollections.CRM_CALL)) {

            CallWithBLOBs call;

            if (data.getParams() instanceof Integer) {
                CallService callService = ApplicationContextUtil.getSpringBean(CallService.class);
                call = callService.findByPrimaryKey((Integer) data.getParams(), AppContext.getAccountId());
                if (call == null) {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            } else if (data.getParams() instanceof CallWithBLOBs) {
                call = (CallWithBLOBs) data.getParams();
            } else {
                throw new MyCollabException("Invalid data: " + data);
            }

            super.onGo(container, data);
            view.editItem(call);
            if (call.getId() == null) {
                AppContext.addFragment("crm/activity/call/add/", AppContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE, "Call"));
            } else {
                AppContext.addFragment("crm/activity/call/edit/" + UrlEncodeDecoder.encode(call.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, "Call", call.getSubject()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    public void save(CallWithBLOBs item) {
        CallService callService = ApplicationContextUtil.getSpringBean(CallService.class);

        item.setSaccountid(AppContext.getAccountId());
        if (item.getId() == null) {
            callService.saveWithSession(item, AppContext.getUsername());
        } else {
            callService.updateWithSession(item, AppContext.getUsername());
        }

    }
}
