package org.semantic.vacationDestination.api.generated;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;


public final class DefaultDestinationResourceTest extends org.semantic.vacationDestination.api.generated.AbstractResourceTest
{
	/**
	 * Server side root resource /destination,
	 * evaluated with some default value(s).
	 */
	private static final String ROOT_RESOURCE_PATH = "/destination";

	/* get() /destination */
	@Test
	public void testGet()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");

		final Response response = target.request().get();

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.OK.getStatusCode(), response.getStatus());
	}

	/* post(entity) /destination */
	@Test
	public void testPostWithDestination()
	{
		final WebTarget target = getRootTarget(ROOT_RESOURCE_PATH).path("");
		final Destination entityBody =
		new Destination();
		final javax.ws.rs.client.Entity<Destination> entity =
		javax.ws.rs.client.Entity.entity(entityBody,"application/json");

		final Response response = target.request().post(entity);

		Assert.assertNotNull("Response must not be null", response);
		Assert.assertEquals("Response does not have expected response code", Status.CREATED.getStatusCode(), response.getStatus());
	}

	@Override
	protected ResourceConfig configureApplication()
	{
		final ResourceConfig application = new ResourceConfig();
		application.register(DefaultDestinationResource.class);
		return application;
	}
}
