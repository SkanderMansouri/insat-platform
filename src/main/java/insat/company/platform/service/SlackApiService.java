package insat.company.platform.service;

import insat.company.platform.domain.Integration;

public interface SlackApiService {

    Integration authenticate(String code);
}
