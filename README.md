# Temporal Networks

## Description

This project aims to provide to GraphStream a new kind of graph modeling temporal networks. In such graph, elements that
have been added to the graph, will be kept inside the graph while they are removed. A temporal network will be an aggregation
over the time of elements. A `TimeWindow` is a time interval, described with an inclusive start date and an exclusive end date,
modeling the presence of the element on the graph for this period of time. For each *temporal* element, one has access to
time-windows of the element.
