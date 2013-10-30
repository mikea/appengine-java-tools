package com.mikea.gae.rx

import com.google.inject.servlet.ServletModule
import com.googlecode.objectify.ObjectifyService
import com.mikea.gae.rx.model.AppVersion

class RxModule extends ServletModule {
  protected override def configureServlets(): Unit = {
    super.configureServlets()

    ObjectifyService.register(classOf[AppVersion])

    serve(RxUrls.RX_URL_BASE + "*").`with`(classOf[RxServlet])
    bind(classOf[Rx]).to(classOf[RxImpl])
  }
}