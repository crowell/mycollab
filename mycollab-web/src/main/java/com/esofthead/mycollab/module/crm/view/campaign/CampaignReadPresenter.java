/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.events.*;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractRelatedListHandler;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignReadPresenter extends CrmGenericPresenter<CampaignReadView> {
    private static final long serialVersionUID = 1L;

    public CampaignReadPresenter() {
        super(CampaignReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleCampaign>() {
            @Override
            public void onEdit(SimpleCampaign data) {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleCampaign data) {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleCampaign data) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    CampaignService campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                                    campaignService.removeWithSession(data,
                                            AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null));
                                }
                            }
                        });
            }

            @Override
            public void onClone(SimpleCampaign data) {
                SimpleCampaign cloneData = (SimpleCampaign) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new CampaignEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleCampaign data) {
                CampaignService contactService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                CampaignSearchCriteria criteria = new CampaignSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                Integer nextId = contactService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new CampaignEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleCampaign data) {
                CampaignService contactService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                CampaignSearchCriteria criteria = new CampaignSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                Integer nextId = contactService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new CampaignEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });

        view.getRelatedActivityHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleActivity>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                if (itemId.equals("task")) {
                    SimpleTask task = new SimpleTask();
                    task.setType(CrmTypeConstants.CAMPAIGN);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(CampaignReadPresenter.this, task));
                } else if (itemId.equals("meeting")) {
                    SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.CAMPAIGN);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(
                            CampaignReadPresenter.this, meeting));
                } else if (itemId.equals("call")) {
                    SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.CAMPAIGN);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(CampaignReadPresenter.this, call));
                }
            }
        });

        view.getRelatedAccountHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleAccount>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleAccount account = new SimpleAccount();
                account.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new AccountEvent.GotoEdit(CampaignReadPresenter.this, account));

            }

            @Override
            public void selectAssociateItems(Set<SimpleAccount> items) {
                if (items.size() > 0) {
                    SimpleCampaign campaign = view.getItem();
                    List<CampaignAccount> associateAccounts = new ArrayList<>();
                    for (SimpleAccount account : items) {
                        CampaignAccount assoAccount = new CampaignAccount();
                        assoAccount.setAccountid(account.getId());
                        assoAccount.setCampaignid(campaign.getId());
                        assoAccount.setCreatedtime(new GregorianCalendar().getTime());
                        associateAccounts.add(assoAccount);
                    }

                    CampaignService accountService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                    accountService.saveCampaignAccountRelationship(associateAccounts, AppContext.getAccountId());

                    view.getRelatedAccountHandlers().refresh();
                }
            }
        });

        view.getRelatedContactHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleContact>() {

            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleContact contact = new SimpleContact();
                contact.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(CampaignReadPresenter.this, contact));
            }

            @Override
            public void selectAssociateItems(Set<SimpleContact> items) {
                if (items.size() > 0) {
                    SimpleCampaign campaign = view.getItem();
                    List<CampaignContact> associateContacts = new ArrayList<>();
                    for (SimpleContact contact : items) {
                        CampaignContact associateContact = new CampaignContact();
                        associateContact.setCampaignid(campaign.getId());
                        associateContact.setContactid(contact.getId());
                        associateContact.setCreatedtime(new GregorianCalendar().getTime());
                        associateContacts.add(associateContact);
                    }

                    CampaignService campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                    campaignService.saveCampaignContactRelationship(associateContacts, AppContext.getAccountId());

                    view.getRelatedContactHandlers().refresh();
                }
            }
        });

        view.getRelatedLeadHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleLead>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleLead lead = new SimpleLead();
                lead.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(CampaignReadPresenter.this, lead));
            }

            @Override
            public void selectAssociateItems(Set<SimpleLead> items) {
                if (items.size() > 0) {
                    SimpleCampaign campaign = view.getItem();
                    List<CampaignLead> associateLeads = new ArrayList<>();
                    for (SimpleLead lead : items) {
                        CampaignLead associateLead = new CampaignLead();
                        associateLead.setCampaignid(campaign.getId());
                        associateLead.setLeadid(lead.getId());
                        associateLead.setCreatedtime(new GregorianCalendar().getTime());
                        associateLeads.add(associateLead);
                    }

                    CampaignService campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                    campaignService.saveCampaignLeadRelationship(associateLeads, AppContext.getAccountId());

                    view.getRelatedLeadHandlers().refresh();
                }
            }
        });

    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CAMPAIGN);
        if (AppContext.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
            if (data.getParams() instanceof Integer) {
                CampaignService campaignService = ApplicationContextUtil.getSpringBean(CampaignService.class);
                SimpleCampaign campaign = campaignService.findById((Integer) data.getParams(), AppContext.getAccountId());
                if (campaign != null) {
                    super.onGo(container, data);
                    view.previewItem(campaign);
                    AppContext.addFragment(CrmLinkGenerator.generateCampaignPreviewLink(campaign.getId()),
                            AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, "Campaign", campaign.getCampaignname()));
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
