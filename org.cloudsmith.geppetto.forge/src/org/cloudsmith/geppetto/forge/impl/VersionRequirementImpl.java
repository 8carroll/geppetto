/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package org.cloudsmith.geppetto.forge.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudsmith.geppetto.forge.ForgeFactory;
import org.cloudsmith.geppetto.forge.ForgePackage;
import org.cloudsmith.geppetto.forge.MatchRule;
import org.cloudsmith.geppetto.forge.VersionRequirement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Version Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.cloudsmith.geppetto.forge.impl.VersionRequirementImpl#getVersion <em>Version</em>}</li>
 * <li>{@link org.cloudsmith.geppetto.forge.impl.VersionRequirementImpl#getMatchRule <em>Match Rule</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VersionRequirementImpl extends EObjectImpl implements VersionRequirement {
	public static class VersionComparator implements Comparator<String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(String av, String bv) {
			List<Object> asegs = parse(av);
			List<Object> bsegs = parse(bv);
			int adx = 0;
			int aTop = asegs.size();
			int bdx = 0;
			int bTop = bsegs.size();
			while(adx < aTop || bdx < bTop) {
				Object a = null;
				Object b = null;
				if(adx < aTop)
					a = asegs.get(adx);
				if(bdx < bTop)
					b = bsegs.get(bdx);

				if(a instanceof Integer) {
					Integer aseg = (Integer) a;
					Integer bseg;

					if(adx < aTop)
						++adx;

					if(b instanceof Integer) {
						bseg = (Integer) b;
						if(bdx < bTop)
							++bdx;
					}
					else
						bseg = ZERO;

					int cmp = aseg.intValue() - bseg.intValue();
					if(cmp == 0)
						continue;
					return cmp;
				}

				if(b instanceof Integer) {
					Integer bseg = (Integer) b;
					Integer aseg;

					if(bdx < bTop)
						++bdx;

					if(a instanceof Integer) {
						aseg = (Integer) a;
						if(adx < aTop)
							++adx;
					}
					else
						aseg = ZERO;

					int cmp = aseg.intValue() - bseg.intValue();
					if(cmp == 0)
						continue;
					return cmp;
				}

				// We are comparing two strings
				if(b == null)
					// a is less since lack of string is greater than a string
					return -1;

				if(a == null)
					return 1;

				int cmp = ((String) a).compareTo((String) b);
				if(cmp != 0)
					return cmp;

				if(adx < aTop)
					++adx;
				if(bdx < bTop)
					++bdx;
			}
			return 0;
		}
	}

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	private static int nextSeparator(String str, int beginIdx) {
		int top = str.length();
		while(beginIdx < top) {
			char c = str.charAt(beginIdx);
			if(c == '.' || c == '-')
				return beginIdx;
			++beginIdx;
		}
		return -1;
	}

	/**
	 * @param string
	 * @return
	 */
	public static VersionRequirement parseVersionRequirement(String versionRequirement) {
		if(versionRequirement == null)
			return null;

		int len = versionRequirement.length();
		if(len == 0)
			return null;

		int idx = 0;
		MatchRule rule = null;
		char c = versionRequirement.charAt(0);
		if(c == '>') {
			++idx;
			if(len > 1 && versionRequirement.charAt(1) == '=') {
				rule = MatchRule.GREATER_OR_EQUAL;
				++idx;
			}
			else
				rule = MatchRule.GREATER;
		}
		else if(c == '<') {
			++idx;
			if(len > 1 && versionRequirement.charAt(1) == '=') {
				rule = MatchRule.LESS_OR_EQUAL;
				++idx;
			}
			else
				rule = MatchRule.LESS;
		}
		else if(c == '=') {
			++idx;
			if(len > 1 && versionRequirement.charAt(1) == '=') {
				rule = MatchRule.PERFECT;
				++idx;
			}
			else
				rule = MatchRule.EQUIVALENT;
		}
		else if(c == '~') {
			++idx;
			rule = MatchRule.COMPATIBLE;
		}
		else {
			if(Character.isLetterOrDigit(c))
				rule = MatchRule.PERFECT;
		}

		if(rule == null)
			throw new IllegalArgumentException("Illegal version operator: " + versionRequirement);

		if(idx > 0) {
			for(; idx < len; ++idx) {
				// We allow space between operator and version
				if(!Character.isWhitespace(versionRequirement.charAt(idx)))
					break;
			}
		}
		if(idx == len)
			throw new IllegalArgumentException("Empty version: " + versionRequirement);

		VersionRequirement vr = ForgeFactory.eINSTANCE.createVersionRequirement();
		vr.setMatchRule(rule);
		vr.setVersion(versionRequirement.substring(idx));
		return vr;
	}

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getMatchRule() <em>Match Rule</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMatchRule()
	 * @generated
	 * @ordered
	 */
	protected static final MatchRule MATCH_RULE_EDEFAULT = MatchRule.PERFECT;

	private static List<Object> parse(String version) {
		if(version == null || version.length() == 0)
			return Collections.emptyList();

		ArrayList<Object> segments = new ArrayList<Object>();
		int dotIdx = nextSeparator(version, 0);
		int beginIdx = 0;
		while(dotIdx >= 0) {
			addSegments(segments, version.substring(beginIdx, dotIdx));
			beginIdx = dotIdx + 1;
			dotIdx = nextSeparator(version, beginIdx);
		}
		if(beginIdx < version.length())
			addSegments(segments, version.substring(beginIdx));
		return segments;
	}

	/**
	 * The cached value of the '{@link #getMatchRule() <em>Match Rule</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMatchRule()
	 * @generated
	 * @ordered
	 */
	protected MatchRule matchRule = MATCH_RULE_EDEFAULT;

	private List<Object> segments;

	private static final Integer ZERO = Integer.valueOf(0);

	private static void addSegments(List<Object> receiver, String s) {
		int len = s.length();
		int idx;
		for(idx = 0; idx < len; ++idx)
			if(!Character.isDigit(s.charAt(idx)))
				break;

		if(idx > 0) {
			// Segment started with digits. The number deserve
			// a segment of its own.
			if(idx == len) {
				receiver.add(Integer.valueOf(s));
				return;
			}
			receiver.add(Integer.valueOf(s.substring(0, idx)));
			s = s.substring(idx);
		}

		// We do not iterate again. The rule we use is that
		// a string, once started with a non-digit character
		// must be terminated with a delimiter. So:
		//
		// 1b becomes 1.b
		// 1b1b2 becomes 1.b1b2
		//
		receiver.add(s);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VersionRequirementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch(featureID) {
			case ForgePackage.VERSION_REQUIREMENT__VERSION:
				return getVersion();
			case ForgePackage.VERSION_REQUIREMENT__MATCH_RULE:
				return getMatchRule();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch(featureID) {
			case ForgePackage.VERSION_REQUIREMENT__VERSION:
				return VERSION_EDEFAULT == null
						? version != null
						: !VERSION_EDEFAULT.equals(version);
			case ForgePackage.VERSION_REQUIREMENT__MATCH_RULE:
				return matchRule != MATCH_RULE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch(featureID) {
			case ForgePackage.VERSION_REQUIREMENT__VERSION:
				setVersion((String) newValue);
				return;
			case ForgePackage.VERSION_REQUIREMENT__MATCH_RULE:
				setMatchRule((MatchRule) newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ForgePackage.Literals.VERSION_REQUIREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch(featureID) {
			case ForgePackage.VERSION_REQUIREMENT__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ForgePackage.VERSION_REQUIREMENT__MATCH_RULE:
				setMatchRule(MATCH_RULE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String findBestMatch(Iterable<String> versions) {
		String best = null;
		for(String v : versions) {
			if(matches(v)) {
				if(best == null || VERSION_COMPARATOR.compare(best, v) < 0)
					best = v;
			}
		}
		return best;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MatchRule getMatchRule() {
		return matchRule;
	}

	private synchronized List<Object> getSegments() {
		if(segments == null)
			segments = parse(getVersion());
		return segments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean matches(String version) {
		MatchRule rule = getMatchRule();
		List<Object> asegs = parse(version);
		List<Object> bsegs = getSegments();
		int adx = 0;
		int aTop = asegs.size();
		int bdx = 0;
		int bTop = bsegs.size();
		for(int idx = 0; adx < aTop || bdx < bTop; ++idx) {
			Object a = null;
			Object b = null;
			if(adx < aTop)
				a = asegs.get(adx);
			if(bdx < bTop)
				b = bsegs.get(bdx);

			if(a instanceof Integer) {
				Integer aseg = (Integer) a;
				Integer bseg;

				if(adx < aTop)
					++adx;

				if(b instanceof Integer) {
					bseg = (Integer) b;
					if(bdx < bTop)
						++bdx;
				}
				else
					bseg = ZERO;

				int cmp = aseg.intValue() - bseg.intValue();
				if(cmp == 0)
					continue;

				if(cmp < 0)
					return rule == MatchRule.LESS || rule == MatchRule.LESS_OR_EQUAL;

				switch(rule) {
					case PERFECT:
						return false;
					case EQUIVALENT:
						return idx >= 2;
					case COMPATIBLE:
						return idx >= 1;
				}
				return rule == MatchRule.GREATER || rule == MatchRule.GREATER_OR_EQUAL;
			}

			if(b instanceof Integer) {
				Integer bseg = (Integer) b;
				Integer aseg;

				if(bdx < bTop)
					++bdx;

				if(a instanceof Integer) {
					aseg = (Integer) a;
					if(adx < aTop)
						++adx;
				}
				else
					aseg = ZERO;

				int cmp = aseg.intValue() - bseg.intValue();
				if(cmp == 0)
					continue;

				if(cmp < 0)
					return rule == MatchRule.LESS || rule == MatchRule.LESS_OR_EQUAL;

				switch(rule) {
					case PERFECT:
						return false;
					case EQUIVALENT:
						return idx >= 2;
					case COMPATIBLE:
						return idx >= 1;
				}
				return rule == MatchRule.GREATER || rule == MatchRule.GREATER_OR_EQUAL;
			}

			// We are comparing two strings
			if(b == null)
				// a is less since lack of string is greater than a string
				return rule == MatchRule.LESS || rule == MatchRule.LESS_OR_EQUAL;

			int cmp = (a == null)
					? 1
					: ((String) a).compareTo((String) b);
			if(cmp < 0)
				return rule == MatchRule.LESS || rule == MatchRule.LESS_OR_EQUAL;

			if(cmp > 0) {
				switch(rule) {
					case PERFECT:
						return false;
					case EQUIVALENT:
						return idx >= 2;
					case COMPATIBLE:
						return idx >= 1;
				}
				return rule == MatchRule.GREATER || rule == MatchRule.GREATER_OR_EQUAL;
			}

			if(adx < aTop)
				++adx;
			if(bdx < bTop)
				++bdx;
			--idx; // Has no bearing on major, minor, service
		}
		return rule != MatchRule.LESS && rule != MatchRule.GREATER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMatchRule(MatchRule newMatchRule) {
		MatchRule oldMatchRule = matchRule;
		matchRule = newMatchRule == null
				? MATCH_RULE_EDEFAULT
				: newMatchRule;
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(
				this, Notification.SET, ForgePackage.VERSION_REQUIREMENT__MATCH_RULE, oldMatchRule, matchRule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setVersion(String newVersion) {
		if(newVersion != null) {
			if(newVersion.length() == 0)
				newVersion = null;
			else if(!Character.isLetterOrDigit(newVersion.charAt(0)))
				throw new IllegalArgumentException("Bad version: " + newVersion);
		}

		String oldVersion = version;
		synchronized(this) {
			version = newVersion;
			segments = null;
		}
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(
				this, Notification.SET, ForgePackage.VERSION_REQUIREMENT__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if(eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder();
		MatchRule rule = getMatchRule();
		if(rule != null)
			result.append(rule);
		String ver = getVersion();
		if(ver != null)
			result.append(ver);
		return result.toString();
	}

} // VersionRequirementImpl
