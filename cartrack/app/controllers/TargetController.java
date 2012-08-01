package controllers;

import java.util.List;

import models.Account;
import models.Target;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import tools.MD5Util;
import views.html.*;

public class TargetController extends Controller {
	public static Result newTarget() {
		List<Target> targets = Account.find.byId(Long
				.valueOf(session("userId"))).targets;
		Form<Target> newTarget = form(Target.class).bindFromRequest();
		if (!newTarget.hasErrors()) {
			Target targetPO = newTarget.get();
			targetPO.account = Account.find.byId(Long
					.valueOf(session("userId")));
			if (!targetPO.isExist()) {
				targetPO.save();
				return ok(newtarget.render(targets, "创建成功！"));

			} else {
				return badRequest(newtarget.render(targets, "该对象已经存在"));
			}

		} else {
			Target targetPO = new Target();
			targetPO.account = Account.find.byId(Long
					.valueOf(session("userId")));
			return badRequest(newtarget.render(targets, "内容错误"));
		}
	}
}
