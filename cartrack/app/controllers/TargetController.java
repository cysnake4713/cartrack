package controllers;


import models.Account;
import models.Point;
import models.Target;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
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
				return ok(newtarget.render("创建成功！",
						Account.find.byId(Long.valueOf(session("userId")))
								.getUserTargetName()));

			} else {
				point.target = point.target.findByTargetTag();
				point.save();
				return ok(newtarget.render("创建新记录成功",
						Account.find.byId(Long.valueOf(session("userId")))
								.getUserTargetName()));
			}

		} else {
			return badRequest(newtarget.render("输入的格式有误",
					Account.find.byId(Long.valueOf(session("userId")))
							.getUserTargetName()));
		}
	}

	public static Result getTargetInfo(Long id) {
		Target target = Target.find.byId(id);
		if (target != null) {
			System.out.println(target.targetName);
			return ok(targetdetail.render("", target));
		} else {
			return badRequest(targetdetail.render("该对象不存在！", null));
		}
	}
}
