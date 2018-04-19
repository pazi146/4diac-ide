/*
 * generated by Xtext
 */
package org.eclipse.fordiac.ide.model.structuredtext.ui

import com.google.inject.Binder
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.eclipse.xtext.resource.IResourceDescriptions
import org.eclipse.xtext.resource.impl.ResourceSetBasedResourceDescriptions
import org.eclipse.xtext.resource.impl.SimpleResourceDescriptionsBasedContainerManager
import org.eclipse.xtext.ui.shared.Access
import org.eclipse.fordiac.ide.model.structuredtext.converter.StructuredTextValueConverterService
import org.eclipse.fordiac.ide.model.structuredtext.resource.StructuredTextResource
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.conversion.IValueConverterService
import org.eclipse.xtext.ui.editor.model.ResourceForIEditorInputFactory
import org.eclipse.xtext.ui.resource.SimpleResourceSetProvider

/** 
 * Use this class to register components to be used within the IDE.
 */
class StructuredTextUiModule extends AbstractStructuredTextUiModule {
	new(AbstractUIPlugin plugin) {
		super(plugin)
	}

	def Class<? extends XtextResource> bindXtextResource() {
		return StructuredTextResource
	}

	def Class<? extends IContainer.Manager> bindIContainer$Manager() {
		return SimpleResourceDescriptionsBasedContainerManager
	}

	def void configureIResourceDescriptions(Binder binder) {
		binder.bind(IResourceDescriptions).to(ResourceSetBasedResourceDescriptions)
	}

	override provideIAllContainersState() { 
		return Access::getWorkspaceProjectsState()
	}

	def Class<? extends IValueConverterService> bindIValueConverterService() {
		return StructuredTextValueConverterService
	}
	
	override bindIResourceForEditorInputFactory() { return ResourceForIEditorInputFactory; }
	
	override bindIResourceSetProvider() {
    	return SimpleResourceSetProvider;
	}
}
