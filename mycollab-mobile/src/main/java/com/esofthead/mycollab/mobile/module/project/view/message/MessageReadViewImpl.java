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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.FormSectionBuilder;
import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class MessageReadViewImpl extends AbstractMobilePageView implements MessageReadView {
    private static final long serialVersionUID = -2234834060460314620L;

    private SimpleMessage bean;
    private final VerticalLayout mainLayout;

    public MessageReadViewImpl() {
        super();
        this.addStyleName("message-read-view");
        this.setCaption(AppContext.getMessage(MessageI18nEnum.M_VIEW_READ_TITLE));
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        this.setContent(mainLayout);
    }

    @Override
    public void previewItem(SimpleMessage message) {
        this.bean = message;
    }

    private void displayItem() {
        mainLayout.removeAllComponents();
        MHorizontalLayout messageBlock = new MHorizontalLayout().withSpacing(false).withFullWidth().withStyleName("message-block");
        Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(bean.getPostedUserAvatarId(), 32);
        userAvatarImg.setStyleName("user-avatar");
        messageBlock.addComponent(userAvatarImg);

        CssLayout rightCol = new CssLayout();
        rightCol.setWidth("100%");

        MHorizontalLayout metadataRow = new MHorizontalLayout().withFullWidth();

        ELabel userNameLbl = new ELabel(bean.getFullPostedUserName()).withStyleName(UIConstants.META_INFO);
        userNameLbl.addStyleName(UIConstants.TRUNCATE);
        CssLayout userNameWrap = new CssLayout(userNameLbl);

        ELabel messageTimePost = new ELabel().prettyDateTime(bean.getPosteddate()).withStyleName(UIConstants.META_INFO).withWidthUndefined();
        metadataRow.with(userNameWrap, messageTimePost).withAlign(messageTimePost, Alignment.TOP_RIGHT).expand(userNameWrap);

        rightCol.addComponent(metadataRow);

        CssLayout titleRow = new CssLayout();
        titleRow.setWidth("100%");
        titleRow.setStyleName("title-row");
        Label messageTitle = new Label(bean.getTitle());
        messageTitle.setStyleName("message-title");
        titleRow.addComponent(messageTitle);
        rightCol.addComponent(titleRow);

        Label messageContent = new Label(StringUtils.trimHtmlTags(bean.getMessage()));
        rightCol.addComponent(messageContent);

        ResourceService attachmentService = ApplicationContextUtil.getSpringBean(ResourceService.class);
        List<Content> attachments = attachmentService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(
                AppContext.getAccountId(), bean.getProjectid(), ProjectTypeConstants.MESSAGE, "" + bean.getId()));
        if (CollectionUtils.isNotEmpty(attachments)) {
            CssLayout attachmentPanel = new CssLayout();
            attachmentPanel.setStyleName("attachment-panel");
            attachmentPanel.setWidth("100%");

            for (Content attachment : attachments) {
                attachmentPanel.addComponent(MobileAttachmentUtils.renderAttachmentRow(attachment));
            }
            rightCol.addComponent(attachmentPanel);
        }

        messageBlock.with(rightCol).expand(rightCol);
        mainLayout.addComponent(messageBlock);

        Label commentTitleLbl = new Label();
        Component section = FormSectionBuilder.build(FontAwesome.COMMENT, commentTitleLbl);
        MessageCommentListDisplay commentDisplay = new MessageCommentListDisplay(ProjectTypeConstants.MESSAGE, bean
                .getId() + "", bean.getProjectid(), true);
        int numComments = commentDisplay.getNumComments();
        commentTitleLbl.setValue(AppContext.getMessage(GenericI18Enum.TAB_COMMENT, numComments));

        mainLayout.addComponent(section);
        mainLayout.addComponent(commentDisplay);
        this.setToolbar(commentDisplay.getCommentBox());
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        displayItem();
    }

    @Override
    public SimpleMessage getItem() {
        return this.bean;
    }

}
