package controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import models.Account;
import models.Point;
import models.Target;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.newpoint;
import play.mvc.*;

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
			return badRequest(newpoint.render("输入的格式不正确！", userTargetName));
		}

	}

	// @BodyParser.Of(play.mvc.BodyParser.Json.class)
	public static Result getPointsByTarget(Long id) {
		Target target = Target.find.byId(id);
		if (target != null) {
			for (Point point : target.points) {
				point.target = null;
			}
			JsonNode pointsJson = Json.toJson(target.points);
			ObjectNode result = Json.newObject();
			target.points = null;
			target.account = null;
			result.put("target", Json.toJson(target));
			result.put("points", pointsJson);
			return ok(result);
		} else {
			return badRequest();
		}
	}
}
