## Graph Layout Java Swing Component

# Description

XXX is a Java Swing Component aimed at displaying graphs (e.g. nodes connected to each others). Its design objectives are to make it easy to use, extend and useful in the context of experimenting with graph layout algorithms. It is developped at the Cnrs / I3S Compouter Science lab / Université Côte d'Azur / Inria. 

# Features
Main features of XXX include:
- It is written in 100% Java 8 (which still is the most used JDK today).
- It is designed to be efficient, so as to allow the display of very large graphs.
- It is a standard Swing component, which make it embeddable in any Java application (Eclipse application will need an adapter).
- It relies on a polished object-oriented architecture.
- It usage is no intrusive, meaning that you don't need to change your application code to display you data: any object can be added to the displayed graph.
- Nodes can be rendererd using their text label, color, fill color, size and icon. All of these are optional.
- layout algorithms' controls can be represented as GUI widgets, allowing to control the behavior of the layout algorithm in an interactive manner.
- vertices can be selected and dragged using the mouse (thereby forcing their x,y position), independantly of the layout algorithms.

