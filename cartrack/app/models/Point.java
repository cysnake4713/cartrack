package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import play.data.format.*;

import play.db.ebean.Model;

@Entity
@SequenceGenerator(name = "point_seq", initialValue = 1)
public class Point extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_seq")
	public Long id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Target target;

	@Formats.DateTime(pattern = "yyyy-MM-dd hh:mm")
	public Date pointRecordTime;

	public Double longitude;// 经度

	public Double latitude;// 纬度

	public String mananger;// 负责人

	public int product;

	public int gasCost;

	public String scanDocumentUrl;

	public String comments;

	public static Finder<Long, Point> find = new Finder<Long, Point>(
			Long.class, Point.class);

}
