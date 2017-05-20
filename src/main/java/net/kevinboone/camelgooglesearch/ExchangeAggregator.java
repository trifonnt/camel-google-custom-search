package net.kevinboone.camelgooglesearch;

import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.Exchange;

/**
 * Aggregator is called by the splitter component, every time a new search
 * result item is split out of the incoming XML file. It takes the link, title,
 * and HTML snippet passed in the message headers, and builds a fragment of HTML.
 * Each fragment is appended to the complete message, eventually to form a
 * page of search results.
 */
public class ExchangeAggregator implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExch, Exchange newExch) {
		if (oldExch == null) {
			return newExch;
		}

		// Get the relevant data from the message header.
		// Note that the message body contains the data 
		// that is being built up by the aggregation process
		String link = (String) newExch.getIn().getHeader("link");
		String title = (String) newExch.getIn().getHeader("title");
		String snippet = (String) newExch.getIn().getHeader("snippet");

		// Get the existing message body...
		String oldExchBody = oldExch.getIn().getBody(String.class);

		// ... and append to it the new search result ...
		String newBody = oldExchBody + "<a href=\"" + link + "\">" + title + "</a>" 
				+ "<br/>" + snippet + "<p/>\n\n";

		// ... then replace the old message body with the extended version
		// containing the new formatted search result
		oldExch.getIn().setBody(newBody);

		return oldExch;
	}
}
