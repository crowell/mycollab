/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.module.crm.view.opportunity;

import com.esofthead.mycollab.module.crm.view.opportunity.IOpportunityPipelineFunnelChartDashlet;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class OpportunityPipelineFunnelChartDashlet extends CssLayout implements IOpportunityPipelineFunnelChartDashlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void display() {

	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(ViewListener listener) {

	}

}
