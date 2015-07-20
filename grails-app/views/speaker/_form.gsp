<%@ page import="eventmgr.Speaker" %>



<div class="fieldcontain ${hasErrors(bean: speakerInstance, field: 'company', 'error')} required">
	<label for="company">
		<g:message code="speaker.company.label" default="Company" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="company" required="" value="${speakerInstance?.company}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: speakerInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="speaker.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${speakerInstance?.name}"/>

</div>

