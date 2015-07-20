<%@ page import="eventmgr.Venue" %>



<div class="fieldcontain ${hasErrors(bean: venueInstance, field: 'address', 'error')} required">
	<label for="address">
		<g:message code="venue.address.label" default="Address" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="address" required="" value="${venueInstance?.address}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: venueInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="venue.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${venueInstance?.name}"/>

</div>

