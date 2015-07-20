<%@ page import="eventmgr.Event" %>



<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="event.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${eventInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'start', 'error')} required">
	<label for="start">
		<g:message code="event.start.label" default="Start" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="start" precision="day"  value="${eventInstance?.start}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'end', 'error')} ">
	<label for="end">
		<g:message code="event.end.label" default="End" />
		
	</label>
	<g:datePicker name="end" precision="day"  value="${eventInstance?.end}" default="none" noSelection="['': '']" />

</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'venue', 'error')} ">
	<label for="venue">
		<g:message code="event.venue.label" default="Venue" />
		
	</label>
	<g:select id="venue" name="venue.id" from="${eventmgr.Venue.list()}" optionKey="id" value="${eventInstance?.venue?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'speakers', 'error')} ">
	<label for="speakers">
		<g:message code="event.speakers.label" default="Speakers" />
		
	</label>
	<g:select name="speakers" from="${eventmgr.Speaker.list()}" multiple="multiple" optionKey="id" size="5" value="${eventInstance?.speakers*.id}" class="many-to-many"/>

</div>

