package controllers;

import models.Account;
import models.Target;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	public static Result register() {
		return ok(register.render(""));
	}

	public static Result login() {
		return ok(login.render(""));
	}

	public static Result newTarget() {
		return ok(newtarget.render(
				Account.find.byId(Long.valueOf(session("userId"))).targets, ""));
	}

	public static Result newPoint() {
		return ok(newpoint.render("",
				Account.find.byId(Long.valueOf(session("userId"))).getUserTargetName()));
	}
}