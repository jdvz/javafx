module ru.dzera.test.ktpostman {
  requires java.base;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires javafx.swing;
  requires javafx.media;
  requires javafx.web;

  requires kotlin.stdlib;
  requires kotlin.reflect;
  requires org.apache.httpcomponents.core5.httpcore5;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires org.apache.httpcomponents.client5.httpclient5;
  requires org.slf4j;

  exports ru.dzera.test.ktpostman;
  opens ru.dzera.test.ktpostman to javafx.fxml;
  opens ru.dzera.test.ktpostman.controller to javafx.fxml, javafx.controls, javafx.base;
  opens ru.dzera.test.ktpostman.model to javafx.base;
}