package models;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import play.data.validation.*;

import play.db.ebean.Model;
import play.i18n.Messages;
import tools.MD5Util;

/**
 * Account store the username and password.
 * */
@Entity
@SequenceGenerator(name = "account_seq", initialValue = 1)
public class Account extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6963943099984043169L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
	public Long id;

	@Column(unique = true)
	@Constraints.Required
	public String accountName;

	@Constraints.Required
	public String password;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@OrderBy("id")  
	public List<Target> targets;

	public static Finder<Long, Account> find = new Finder<Long, Account>(
			Long.class, Account.class);

	public boolean isNameExist() {
		Account account = find.where().eq("accountName", accountName)
				.findUnique();
		return (account == null ? false : true);
	}

	public boolean isExist() {
		Account account = find.where().eq("accountName", accountName)
				.eq("password", MD5Util.getMD5String(password)).findUnique();
		if (account != null) {
			id = account.id;
		}
		return (account == null ? false : true);
	}

	public String getUserTargetName() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (Target target : targets) {
			stringBuilder.append("\"" + target.targetTag + "\",");
		}
		stringBuilder
				.delete(stringBuilder.length() - 1, stringBuilder.length());
		stringBuilder.append("]");

		return stringBuilder.toString();

	}

	@Override
	public String toString() {
		return String.format("id=%d accountName=%s password=%s", id,
				accountName, password);
	}

	public String validate() {
		// account name and password can't be null
		if (accountName == null || "".equals(accountName)) {
			return Messages.get("error_register_accountname_null");
		}
		if (password == null || "".equals(password)) {
			return Messages.get("error_register_password_null");
		}
		// account name must be email type
		Pattern emailPattern = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		if (!emailPattern.matcher(accountName).matches()) {
			return Messages.get("error_register_accountname_not_email")
					.toString();
		}
		// password must large than 9
		if (password.length() < 9) {
			return Messages.get("error_register_password_too_short");
		}
		// password must small than 17
		if (password.length() > 16) {
			return Messages.get("error_register_password_too_long");
		}
		// password must only contain letters and numbers and underline
		Pattern passwordPartern = Pattern
				.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
		if (!passwordPartern.matcher(password).matches()) {
			return Messages.get("error_register_password_bad_char");
		}
		return null;
	}
}
