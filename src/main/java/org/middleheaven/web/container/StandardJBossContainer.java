/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import javax.servlet.ServletContext;



public class StandardJBossContainer extends JBossContainer {

    public StandardJBossContainer(ServletContext context) {
        super(context);
    }

}