package acme.features.learner.dashboard;
/*
 * AuthenticatedConsumerCreateService.java
 *
 * Copyright (C) 2012-2022 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

import java.util.EnumMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.help_requests.HelpRequestStatus;
import acme.forms.LearnerDashboard;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Learner;

@Service
public class LearnerDashboardShowService implements AbstractShowService<Learner, LearnerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LearnerDashboardRepository repository;

	@Override
	public boolean authorise(final Request<LearnerDashboard> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<LearnerDashboard> request, final LearnerDashboard entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		request.unbind(entity, model,
			"totalNumberOfHelpRequestByStatus", "averageHelpRequestsBudgetByStatus", "deviationHelpRequestsBudgetByStats", "minimumHelpRequestsBudgetByStats", "maximumHelpRequestsBudgetByStats");
	}

	@Override
	public LearnerDashboard findOne(final Request<LearnerDashboard> request) {
		assert request != null;

		final LearnerDashboard result = new LearnerDashboard();

		final EnumMap<HelpRequestStatus, Integer> mapTotalNumberHR = new EnumMap<>(HelpRequestStatus.class);
		final EnumMap<HelpRequestStatus, Double> mapAverageHR = new EnumMap<>(HelpRequestStatus.class);
		final EnumMap<HelpRequestStatus, Double> mapDeviationHR = new EnumMap<>(HelpRequestStatus.class);
		final EnumMap<HelpRequestStatus, Double> mapMinumumHR = new EnumMap<>(HelpRequestStatus.class);
		final EnumMap<HelpRequestStatus, Double> mapMaximumHR = new EnumMap<>(HelpRequestStatus.class);

		for(final HelpRequestStatus status: HelpRequestStatus.values()) {
			for(final Object[] obj: this.repository.operationsHelpRequest(status)) {
				mapTotalNumberHR.put(status, Integer.valueOf(obj[0].toString()));
				mapAverageHR.put(status, Double.valueOf(obj[1].toString()));
				mapDeviationHR.put(status, Double.valueOf(obj[2].toString()));
				mapMinumumHR.put(status, Double.valueOf(obj[3].toString()));
				mapMaximumHR.put(status, Double.valueOf(obj[4].toString()));
			}
		}

		result.setTotalNumberOfHelpRequestByStatus(mapTotalNumberHR);
		result.setAverageHelpRequestsBudgetByStatusGroupedByCurrency(mapAverageHR);
		result.setDeviationHelpRequestsBudgetByStatusGroupedByCurrency(mapDeviationHR);
		result.setMinimumHelpRequestsBudgetByStatusGroupedByCurrency(mapMinumumHR);
		result.setMaximumHelpRequestsBudgetByStatusGroupedByCurrency(mapMaximumHR);
		return result;
	}

	// Internal state ---------------------------------------------------------

}