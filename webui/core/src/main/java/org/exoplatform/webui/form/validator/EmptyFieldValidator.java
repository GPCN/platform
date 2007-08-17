/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.form.validator;

import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.exception.MessageException;
import org.exoplatform.webui.form.UIFormInput;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minhdv81@yahoo.com
 * Jun 7, 2006
 * 
 * Validates whether a field is empty
 */
public class EmptyFieldValidator implements Validator {
  
  public void validate(UIFormInput uiInput) throws Exception {
    if((uiInput.getValue() != null) && ((String)uiInput.getValue()).trim().length() > 0) {
      return ;
    }    
    String label = uiInput.getLabel();
    if(label == null) label = uiInput.getName();
    label = label.trim();
    if(label.charAt(label.length() - 1) == ':') label = label.substring(0, label.length() - 1);
    Object[]  args = {label, uiInput.getBindingField() } ;
    throw new MessageException(new ApplicationMessage("EmptyFieldValidator.msg.empty-input", args, 
                                                      ApplicationMessage.WARNING)) ;
  }
}
