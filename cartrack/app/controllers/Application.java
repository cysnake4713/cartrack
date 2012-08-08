package controllers;

import models.Account;
import models.Point;
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
		return ok(newtarget.render("",
				Account.find.byId(Long.valueOf(session("userId")))
						.getUserTargetName()));
	}

	public static Result newPoint(Long id) {
		Target target = Target.find.byId(id);
		return ok(newpoint.render("",target));
	}
	
	public static Result updatePoint(Long id){
		Point point = Point.find.byId(id);
		System.out.println("testet  "+point.target.targetName);
		return ok(updatepoint.render("",form(Point.class).fill(point)));
	}
}