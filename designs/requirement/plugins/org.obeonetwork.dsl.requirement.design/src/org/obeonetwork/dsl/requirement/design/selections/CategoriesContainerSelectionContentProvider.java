package org.obeonetwork.dsl.requirement.design.selections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.obeonetwork.dsl.requirement.CategoriesContainer;

/**
 * @author atakarabt
 *
 */
public class CategoriesContainerSelectionContentProvider extends
		AbstractSelectionContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.
	 * Object)
	 */
	public Object[] getElements(Object inputElement) {
		CategoriesContainer rootContainer = (CategoriesContainer) EcoreUtil
				.getRootContainer((EObject) inputElement);
		return new Object[] { rootContainer };
	}

}
