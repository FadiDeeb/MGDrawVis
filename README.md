# MGDrawVis
MGDrawVis (Metaheuristics Graph Drawing Visualizer) is a Java-based visualization tool that facilitates the comparison of different metaheuristic algorithms for automatic graph layout. 
MGDrawVis offers a wide range of functionalities to users, including the ability to draw a graph by adding nodes and edges using mouse clicks or by uploading a file in Microsoft Excel Worksheet format. Users can also manually change the layout of the graph by dragging the nodes to the required positions, thus providing greater flexibility in choosing the initial layout. The tool can generate single or multiple randomly connected graphs using the Erdős–Rényi model, and it allows the user to save the layout for future use. 
One of the key features of MGDrawVis is its ability to work with a weighted objective function, which allows the user to change the weights of each metric, including desired distances such as node-node occlusion. The tool provides a button for each metaheuristic algorithm, and when a method is selected, the user can adjust the values of the algorithm's parameters. Once the layout optimization is complete, MGDrawVis displays the values of each metric along with the total value of the objective function, number of function evaluations, and execution time in seconds.
In addition, MGDrawVis supports batch file testing, allowing users to upload multiple files and run a chosen metaheuristic algorithm for three different scenarios: finding the best layout that can be generated by the method, finding the best layout that can be generated within a fixed number of function evaluations, or optimizing for a desired objective function value. The tool generates a Microsoft Excel Worksheet for each experiment, including the maximum, minimum, average, and median objective function values, number of function evaluations, and execution time in seconds for all runs. It also generates a PNG file for the output layout of each test case.
Overall, MGDrawVis provides a powerful and flexible tool for evaluating and comparing different metaheuristic algorithms for automatic graph layout. Its intuitive user interface and support for a wide range of functionalities make it an attractive option for researchers and practitioners in the field.
