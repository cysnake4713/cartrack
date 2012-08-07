package controllers;

import java.util.List;

import models.Account;
import models.Point;
import models.Target;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import tools.MD5Util;
import views.html.*;

public class TargetController extends Controller {
	@SuppressWarnings("unchecked")
	public static Result newTarget() {
		Form<Point> newPoint = form(Point.class).bindFromRequest();
		if (!newPoint.hasErrors()) {
			Point point = newPoint.get();
			point.target.account = (Account) Account.find.byId(Long
					.valueOf(session("userId")));
			if (!point.target.isExist()) {
				point.target.save();
				point.target = point.target.findByTargetTag();
				point.save();
				return ok(newtarget.render("创建成功！"));

			} else {
				point.target = point.target.findByTargetTag();
				point.save();
				return ok(newtarget.render("创建新记录成功"));
			}

		} else {
			return badRequest(newtarget.render("输入的格式有误"));
		}
	}
}
