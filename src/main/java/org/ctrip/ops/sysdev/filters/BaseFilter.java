package org.ctrip.ops.sysdev.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ctrip.ops.sysdev.render.FreeMarkerRender;
import org.ctrip.ops.sysdev.render.TemplateRender;

public class BaseFilter{

	private static final Logger logger = Logger.getLogger(BaseFilter.class
			.getName());

	protected Map config;
	protected List<TemplateRender> IF;
	protected TemplateRender render;

	public BaseFilter(Map config) {
		this.config = config;

		if (this.config.containsKey("if")) {
			IF = new ArrayList<TemplateRender>();
			for (String c : (List<String>) this.config.get("if")) {
				try {
					IF.add(new FreeMarkerRender(c, c));
				} catch (IOException e) {
					logger.fatal(e.getMessage());
					System.exit(1);
				}
			}
		} else {
			IF = null;
		}

		this.prepare();
	}

	protected void prepare() {
	};

	public Map process(Map event) {
		boolean succuess = true;
		if (this.IF != null) {
			for (TemplateRender render : this.IF) {
				if (!render.render(event).equals("true")) {
					succuess = false;
					break;
				}
			}
		}
		if (succuess == true) {
			event = this.filter(event);
		}

		return event;
	};

	protected Map filter(Map event) {
		return null;
	}
}
