function drawForm(form, log, xmlData, templateForm) {
     log.log(typeof form);
     log.log(xmlData);
     log.log(templateForm);

     for (i = 0; i<100; ++i) {
         form.drawLabel(1,(i*50),100,200,'hi ' + i + 'from js');
     }
 }