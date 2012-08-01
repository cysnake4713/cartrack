package controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import models.Account;
import models.Target;

import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import tools.MD5Util;
import views.html.*;

public class AccountController extends Controller {

	public static Result register() {
		Form<Account> newAccount = form(Account.class).bindFromRequest();
		if (!newAccount.hasErrors()) {
			Account account = newAccount.get();
			if (!account.isNameExist()) {
				// save the password as md5 style
				String md5Password = MD5Util.getMD5String(account.password);
				account.password = md5Password;
				account.save();
				return ok(login.render(""));
			} else {
				return badRequest(register.render(Messages
						.get("error_register_accountname_is_exist")));
			}

		} else {
			return badRequest(register.render(newAccount.error("").message()));
		}
	}

	public static Result login() {
		Form<Account> accountForm = form(Account.class).bindFromRequest();
		if (!accountForm.hasErrors()) {

			Account account = accountForm.get();
			// if account is exist
			if (account.isExist()) {
				// login and set cookie
				session("userName", account.accountName);
				session("userId", account.id.toString());
				return ok(newtarget
						.render(Account.find.byId(Long
								.valueOf(session("userId"))).targets, ""));
			} else {
				// else return password or account name error message
				return badRequest(login.render(Messages
						.get("error_login_account_is_not_match")));
			}
		} else {
			JsonNode errorsAsJson = accountForm.errorsAsJson();
			Logger.debug(errorsAsJson.toString());
			return badRequest(login.render(accountForm.error("").message()));
		}
	}

	public static Result logout() {
		session().clear();
		return ok(login.render(""));

	}
}
