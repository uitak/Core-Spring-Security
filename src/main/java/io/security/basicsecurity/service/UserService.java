package io.security.basicsecurity.service;

import java.util.List;

import io.security.basicsecurity.domain.Account;
import io.security.basicsecurity.domain.AccountDto;

public interface UserService {

	void createUser(Account account);
}
