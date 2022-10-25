import mine.extensions.GraphicsPanel
import mine.extensions.painter.*
import java.awt.Color
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Point
import java.awt.event.*
import javax.swing.*

class MainWindow : JFrame() {

    val startSize = Dimension(800, 600);
    val minSize = Dimension(800, 600);


    // panels
    val windowPanelColor = Color(85, 91, 110);
    val windowPanel = JPanel();

    val mainPanelColor = Color.WHITE;
    val mainPanel = GraphicsPanel();

    val controlPanelColor = Color(190, 227, 219);
    val controlPanel = JPanel();

    // spinners
    val xMinLabel = JLabel("X min");
    val xMinSpinner = JSpinner();

    val xMaxLabel = JLabel("X max");
    val xMaxSpinner = JSpinner();

    val yMinLabel = JLabel("Y min");
    val yMinSpinner = JSpinner();

    val yMaxLabel = JLabel("Y max");
    val yMaxSpinner = JSpinner();

    // checkboxes
    val pointBox = JCheckBox().also {
        it.isBorderPaintedFlat = true;
        it.background = controlPanelColor;
        it.isSelected = true;

    };

    val functionBox = JCheckBox().also {
        it.isBorderPaintedFlat = true;
        it.background = controlPanelColor;
        it.isSelected = true;
    };

    val derivBox = JCheckBox().also {
        it.isBorderPaintedFlat = true;
        it.background = controlPanelColor;
        it.isSelected = true;
    };

    // color pickers
    var funcColor = Color(137, 176, 174);
    val funcColorPickerLabel = JLabel("Function color");
    val funcColorPickerPanel = JPanel();

    var pointColor = Color(255, 214, 186);
    val pointColorPickerLabel = JLabel("Point color");
    val pointColorPickerPanel = JPanel();

    var derivColor = Color.BLUE;
    val derivColorPickerLabel = JLabel("Derivative color");
    val derivColorPickerPanel = JPanel();


    companion object {
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }

    init {  // TODO: implement tools as TOOL STATE MACHINE

        size = startSize;
        minimumSize = minSize;
        defaultCloseOperation = EXIT_ON_CLOSE;
        layout = null;
        setLocationRelativeTo(null); // creates frame in the center of the screen

        // setup panels color
        windowPanel.background = windowPanelColor;
        controlPanel.background = controlPanelColor;
        this.contentPane.background = Color(85, 91, 110);

        controlPanel.foreground = Color(85, 91, 110); // todo: change text's color and change
        mainPanel.background = mainPanelColor;


        // setup spinners changeListeners
        SetupSpinners();
        SetupColorPickers();
        SetupLayout();

        this.isVisible = true;

        SetupPainters();

        SetupEventListeners();


        pack();

    }

    private fun SetupEventListeners() {  // TODO: wrong

        val axisPainter = mainPanel.getPainter("AxisPainter");
        axisPainter as AxisPainter?;

        mainPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(componentEvent: ComponentEvent) {
                axisPainter?.width = mainPanel.width;
                axisPainter?.height = mainPanel.height;
                mainPanel.repaint();
            }
        })

        xMinSpinner.addChangeListener()
        {

            axisPainter?.xMin = xMinSpinner.value as Double;
            mainPanel.repaint();

        }

        xMaxSpinner.addChangeListener()
        {
            axisPainter?.xMax = xMaxSpinner.value as Double;
            mainPanel.repaint();
        }

        yMinSpinner.addChangeListener()
        {
            axisPainter?.yMin = yMinSpinner.value as Double;
            mainPanel.repaint();
        }

        yMaxSpinner.addChangeListener()
        {
            axisPainter?.yMax = yMaxSpinner.value as Double;
            mainPanel.repaint();
        }


        // panel mouse events

        // clicked
        this.mainPanel.addMouseListener(object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent?) {
                e?.apply {

                    if (e.button == MouseEvent.BUTTON1) {
                        PointPainter.AddPoint(Point(x, y));
                        FunctionPainter.AddPoint(Point(x, y));
                    }
                    if (e.button == MouseEvent.BUTTON3) {
                        PointPainter.RemovePoint(Point(x, y));
                        FunctionPainter.RemovePoint(Point(x, y));
                    }

                    mainPanel.repaint();
                }
            }
        })

        // moved
        this.mainPanel.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent?) {
                e?.apply {
                    MouseLocationPainter.MouseLocation = Point(x, y);
                    mainPanel.repaint();
                }
            }
        })

        // checkbox listeners
        functionBox.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                FunctionPainter.isVisible = e?.stateChange == 1;
                mainPanel.repaint();
            }
        })

        pointBox.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                PointPainter.isVisible = e?.stateChange == 1;
                mainPanel.repaint();

            }
        })

        derivBox.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                DerivativePainter.isVisible = e?.stateChange == 1;
                mainPanel.repaint();
            }
        })
    }

    private fun SetupPainters() {

        this.mainPanel.addPainter(
            AxisPainter.also {
                it.width = mainPanel.width;
                it.height = mainPanel.height;
                it.xMin = xMinSpinner.value as Double;
                it.xMax = xMaxSpinner.value as Double;
                it.yMin = yMinSpinner.value as Double;
                it.yMax = yMaxSpinner.value as Double;
            })

        this.mainPanel.addPainter(FunctionPainter.also { it.FunctionColor = funcColor });

        this.mainPanel.addPainter(
            PointPainter.also {
                it.PointColor = pointColor;
            }
        )

        this.mainPanel.addPainter(
            MouseLocationPainter.also { it.MouseLocation = MouseInfo.getPointerInfo().location }
        )


    }

    private fun SetupColorPickers() {

        val HandleMouseClick = fun(panel: JPanel, state: String) {
            panel.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    JColorChooser.showDialog(
                        this@MainWindow,
                        "Choose color",
                        panel.background
                    )?.let {
                        panel.background = it

                        if (state == "func") {
                            funcColor = it
                            FunctionPainter.FunctionColor = it;
                            mainPanel.repaint();
                        };
                        if (state == "point") {
                            pointColor = it;
                            PointPainter.PointColor = it; // todo: do we need to store two vars?
                            mainPanel.repaint();
                        };
                        if (state == "deriv") derivColor = it;

                    }
                }
            })
        };


        funcColorPickerPanel.background = funcColor;
        funcColorPickerPanel.border = BorderFactory.createLineBorder(Color.BLACK);
        HandleMouseClick(funcColorPickerPanel, "func");

        pointColorPickerPanel.background = pointColor;
        pointColorPickerPanel.border = BorderFactory.createLineBorder(Color.BLACK);
        HandleMouseClick(pointColorPickerPanel, "point");

        derivColorPickerPanel.background = derivColor;
        derivColorPickerPanel.border = BorderFactory.createLineBorder(Color.BLACK);
        HandleMouseClick(derivColorPickerPanel, "deriv");


    }


    private fun SetupSpinners() {

        val xMinModel = SpinnerNumberModel(-5.0, -100.0, 4.8, 0.1)
        xMinSpinner.model = xMinModel;

        val xMaxModel = SpinnerNumberModel(5.0, -4.8, 100.0, 0.1)
        xMaxSpinner.model = xMaxModel;

        val yMinModel = SpinnerNumberModel(-5.0, -100.0, 4.8, 0.1)
        yMinSpinner.model = yMinModel;

        val yMaxModel = SpinnerNumberModel(5.0, -4.8, 100.0, 0.1)
        yMaxSpinner.model = yMaxModel;

        xMinSpinner.addChangeListener { _ ->
            xMaxModel.minimum = xMinModel.value as Double + 2.0 * xMinModel.stepSize as Double
        }
        xMaxSpinner.addChangeListener { _ ->
            xMinModel.maximum = xMaxModel.value as Double - 2.0 * xMaxModel.stepSize as Double
        }

        yMinSpinner.addChangeListener { _ ->
            yMaxModel.minimum = yMinModel.value as Double + 2.0 * yMinModel.stepSize as Double
        }
        yMaxSpinner.addChangeListener { _ ->
            yMinModel.maximum = yMaxModel.value as Double - 2.0 * yMaxModel.stepSize as Double
        }

    }

    private fun SetupLayout() {
        SetupPanelsLayout();
        SetupControlPanelLayout();
    }

    private fun SetupPanelsLayout() {

        var gl = GroupLayout(this.contentPane)
        this.layout = gl;

        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addComponent(
                    mainPanel,
                    GROW,
                    GROW,
                    GROW
                )
                .addGap(8)
                .addComponent(
                    controlPanel,
                    GROW,
                    GROW,
                    GROW
                )
        );

        gl.setHorizontalGroup(
            gl.createParallelGroup()
                .addComponent(
                    mainPanel,
                    GROW,
                    GROW,
                    GROW
                )
                .addComponent(
                    controlPanel,
                    GROW,
                    GROW,
                    GROW
                )
        );
    }


    private fun SetupControlPanelLayout() {

        val gl = GroupLayout(this.controlPanel);
        controlPanel.layout = gl;

        gl.setVerticalGroup(
            gl.createParallelGroup()
                .addGroup(
                    gl.createSequentialGroup() // check boxes group
                        .addGap(10)
                        .addComponent(functionBox, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(pointBox, 20, SHRINK, SHRINK)
                        .addGap(8)
                        .addComponent(derivBox, 20, SHRINK, SHRINK)
                )
                .addGroup(
                    gl.createSequentialGroup() // color pickers group
                        .addGap(10)
                        .addComponent(funcColorPickerPanel, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(pointColorPickerPanel, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(derivColorPickerPanel, 20, SHRINK, SHRINK)
                )
                .addGap(10)
                .addGroup(
                    gl.createSequentialGroup() // color picker labels group
                        .addGap(10)
                        .addComponent(funcColorPickerLabel, SHRINK, SHRINK, SHRINK)
                        .addGap(13)
                        .addComponent(pointColorPickerLabel, SHRINK, SHRINK, SHRINK)
                        .addGap(16)
                        .addComponent(derivColorPickerLabel, SHRINK, SHRINK, SHRINK)

                )
                .addGroup(
                    gl.createSequentialGroup() // min group
                        .addComponent(xMinLabel, 10, SHRINK, SHRINK)
                        .addGap(2)
                        .addComponent(xMinSpinner, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(yMinLabel, 10, SHRINK, SHRINK)
                        .addGap(2)
                        .addComponent(yMinSpinner, 20, SHRINK, SHRINK)
                        .addGap(10)

                )
                .addGap(10)

                .addGroup(
                    gl.createSequentialGroup() // max group
                        .addComponent(xMaxLabel, 10, SHRINK, SHRINK)
                        .addGap(2)
                        .addComponent(xMaxSpinner, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(yMaxLabel, 10, SHRINK, SHRINK)
                        .addGap(2)
                        .addComponent(yMaxSpinner, 20, SHRINK, SHRINK)
                        .addGap(10)
                )
        );

        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(10)
                .addGroup(
                    gl.createParallelGroup() // min group
                        .addComponent(xMinLabel, SHRINK, SHRINK, SHRINK)
                        .addComponent(xMinSpinner, SHRINK, SHRINK, SHRINK)
                        .addComponent(yMinLabel, 10, SHRINK, SHRINK)
                        .addComponent(yMinSpinner, 20, SHRINK, SHRINK)
                )
                .addGap(30)
                .addGroup(
                    gl.createParallelGroup() // max group
                        .addComponent(xMaxLabel, 10, SHRINK, SHRINK)
                        .addComponent(xMaxSpinner, 20, SHRINK, SHRINK)
                        .addComponent(yMaxLabel, 10, SHRINK, SHRINK)
                        .addComponent(yMaxSpinner, 20, SHRINK, SHRINK)

                )
                .addGap(10, 10, Int.MAX_VALUE)

                .addGroup(
                    gl.createParallelGroup() // check boxes group
                        .addComponent(functionBox, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(pointBox, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(derivBox, 20, SHRINK, SHRINK)
                )

                .addGroup(
                    gl.createParallelGroup() // color pickers group
                        .addGap(10)
                        .addComponent(funcColorPickerPanel, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(pointColorPickerPanel, 20, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(derivColorPickerPanel, 20, SHRINK, SHRINK)
                        .addGap(10)

                )
                .addGap(10)
                .addGroup(
                    gl.createParallelGroup() // color picker labels group
                        .addComponent(funcColorPickerLabel, SHRINK, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(pointColorPickerLabel, SHRINK, SHRINK, SHRINK)
                        .addGap(10)
                        .addComponent(derivColorPickerLabel, SHRINK, SHRINK, SHRINK)
                        .addGap(10)
                )
                .addGap(10)
        );
    }

}