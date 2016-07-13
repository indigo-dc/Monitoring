// package it.reply.monitoringpillar.test;
//
// import javax.inject.Inject;
//
// import org.jboss.arquillian.container.test.api.Deployment;
// import org.jboss.arquillian.junit.Arquillian;
// import org.jboss.shrinkwrap.api.ShrinkWrap;
// import org.jboss.shrinkwrap.api.asset.EmptyAsset;
// import org.jboss.shrinkwrap.api.spec.JavaArchive;
// import org.junit.Assert;
// import org.junit.Test;
// import org.junit.runner.RunWith;
//
// import it.reply.monitoringpillar.config.Configuration;
//
// @RunWith(Arquillian.class)
// public class Testoftest {
//
// @Inject
// private Configuration config;
//
// @Deployment
// public static JavaArchive createDeployment() {
// JavaArchive jar =
// ShrinkWrap.create(JavaArchive.class).addClass(Configuration.class)
// .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
// System.out.println(jar.toString(true));
// return jar;
// }
//
// @Test
// public void should_create_configFile() {
// Assert.assertEquals("Configuration file created and deployed!",
// config.getZoneNames(),
// config.getMonitoringConfigurations());
// }
// }
