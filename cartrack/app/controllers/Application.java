package controllers;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {


	public static Result register() {
		return ok(register.render(""));
	}

	public static Result login() {
		return ok(login.render(""));
	}
}