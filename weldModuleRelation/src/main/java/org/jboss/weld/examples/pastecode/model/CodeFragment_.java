package org.jboss.weld.examples.pastecode.model;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CodeFragment.class)
public abstract class CodeFragment_ {

	public static volatile SingularAttribute<CodeFragment, Integer> id;
	public static volatile SingularAttribute<CodeFragment, String> text;
	public static volatile SingularAttribute<CodeFragment, String> hash;
	public static volatile SingularAttribute<CodeFragment, Language> language;
	public static volatile SingularAttribute<CodeFragment, Date> datetime;
	public static volatile SingularAttribute<CodeFragment, String> user;

}