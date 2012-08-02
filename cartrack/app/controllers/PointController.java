package controllers;

import models.Account;
import models.Point;
import models.Target;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.newpoint;

public class PointController extends Controller {
	public static Result newPoint() {
		String userTargetName = Account.find.byId(
				Long.valueOf(session("userId"))).getUserTargetName();
		Form<Point> newPoint = form(Point.class).bindFromRequest();
		if (!newPoint.hasErrors()) {
			Point pointPO = newPoint.get();
			Target tempTarget = pointPO.target.findByTargetTag();
			if (tempTarget != null) {
				pointPO.target = tempTarget;
			} else {
				return badRequest(newpoint.render("该对象不存在！", userTargetName));

			}
			pointPO.save();
			return ok(newpoint.render("创建成功", userTargetName));
		} else {
			return badRequest(newpoint.render(newPoint.errorsAsJson()
					.toString(), userTargetName));
		}

	}
}
