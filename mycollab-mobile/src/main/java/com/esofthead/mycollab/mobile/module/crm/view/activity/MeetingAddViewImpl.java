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

import com.esofthead.mycollab.mobile.form.view.DynaFormLayout;
import com.esofthead.mycollab.mobile.ui.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@ViewComponent
public class MeetingAddViewImpl extends AbstractEditItemComp<MeetingWithBLOBs>
		implements MeetingAddView {
	private static final long serialVersionUID = 2739924176536721587L;

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject() != null ? beanItem.getSubject()
				: AppContext.getMessage(MeetingI18nEnum.VIEW_NEW_TITLE);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.MEETING,
				MeetingDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<MeetingWithBLOBs> initBeanFormFieldFactory() {
		return new MeetingEditFormFieldFactory(editForm);
	}

}
