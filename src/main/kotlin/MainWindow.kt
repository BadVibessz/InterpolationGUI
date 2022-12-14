import mine.extensions.GraphicsPanel
import mine.extensions.PolynomialCalculator
import mine.extensions.converter.PixelCoordConverter
import mine.extensions.painter.*
import java.awt.Color
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Point
import java.awt.event.*
import javax.swing.*
import kotlin.math.abs


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

    var derivColor = Color(190, 227, 219);
    val derivColorPickerLabel = JLabel("Derivative color");
    val derivColorPickerPanel = JPanel();

    // buttons
    val clearBtn = JButton("Clear");

    companion object {
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }

    init {

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

        SetupSpinners();
        SetupColorPickers();
        SetupLayout();

        pack();


        SetupPainters();
        SetupEventListeners();
        SetupConverter();

        this.isVisible = true;
    }

    private fun SetupConverter() {

        PixelCoordConverter.width = mainPanel.width;
        PixelCoordConverter.height = mainPanel.height;
        PixelCoordConverter.yMin = yMinSpinner.value as Double;
        PixelCoordConverter.yMax = yMaxSpinner.value as Double;
        PixelCoordConverter.xMin = xMinSpinner.value as Double;
        PixelCoordConverter.xMax = xMaxSpinner.value as Double;
    }

    private fun SetupEventListeners() {

        mainPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(componentEvent: ComponentEvent) {
                PixelCoordConverter.width = mainPanel.width;
                PixelCoordConverter.height = mainPanel.height;
                mainPanel.repaint();
            }
        })

        xMinSpinner.addChangeListener()
        {

            PixelCoordConverter.xMin = xMinSpinner.value as Double;
            mainPanel.repaint();

        }

        xMaxSpinner.addChangeListener()
        {
            PixelCoordConverter.xMax = xMaxSpinner.value as Double;
            mainPanel.repaint();
        }

        yMinSpinner.addChangeListener()
        {
            PixelCoordConverter.yMin = yMinSpinner.value as Double;
            mainPanel.repaint();
        }

        yMaxSpinner.addChangeListener()
        {
            PixelCoordConverter.yMax = yMaxSpinner.value as Double;
            mainPanel.repaint();
        }


        // buttons clicked
        this.clearBtn.addActionListener {

            (mainPanel.getPainter("MainFunctionPainter") as FunctionPainter).Clear();
            (mainPanel.getPainter("DerivativeFunctionPainter") as FunctionPainter).Clear();

            PolynomialCalculator.Clear();
            PointPainter.Clear();

            mainPanel.repaint();
        }


        // panel mouse events

        // clicked
        this.mainPanel.addMouseListener(object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent?) {
                e?.apply {

                    val mainFuncPainter = mainPanel.getPainter("MainFunctionPainter") as FunctionPainter?;
                    val derivFuncPainter = mainPanel.getPainter("DerivativeFunctionPainter") as FunctionPainter?;

                    if (e.button == MouseEvent.BUTTON1) {
                        PointPainter.AddPoint(Point(x, y));
                        PolynomialCalculator.AddPoint(Point(x, y));
                        mainFuncPainter?.function = PolynomialCalculator.GetPolynomial();
                        derivFuncPainter?.function = PolynomialCalculator.GetPolynomialDerivative();
                    }
                    if (e.button == MouseEvent.BUTTON3) {
                        PointPainter.RemovePoint(Point(x, y));
                        PolynomialCalculator.RemovePoint(Point(x, y));
                        mainFuncPainter?.function = PolynomialCalculator.GetPolynomial();
                        derivFuncPainter?.function = PolynomialCalculator.GetPolynomialDerivative();
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

        // scrolled
        this.mainPanel.addMouseWheelListener(object : MouseAdapter() {
            override fun mouseWheelMoved(e: MouseWheelEvent?) {

                e?.apply {

                    val offset = 0.1;
                    var sign = 1;

                    if (e.wheelRotation < 0)
                        sign = -1;

                    val vals = mutableListOf(
                        xMinSpinner.value as Double,
                        xMaxSpinner.value as Double,
                        yMinSpinner.value as Double,
                        yMaxSpinner.value as Double
                    );

                    val minValue = vals.min();
                    val maxValue = vals.max();

                    // borders check
                    if ((abs(minValue) - 0.1 < 1.0 && sign == -1) || (abs(maxValue) + 0.1 > 100 && sign == 1)) return;

                    // x
                    xMinSpinner.value = ((xMinSpinner.value as Double) - sign * offset);
                    xMaxSpinner.value = ((xMaxSpinner.value as Double) + sign * offset);

                    // y
                    yMinSpinner.value = ((yMinSpinner.value as Double) - sign * offset);
                    yMaxSpinner.value = ((yMaxSpinner.value as Double) + sign * offset);

                }


            }
        })

        // checkbox listeners
        functionBox.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                (mainPanel.getPainter("MainFunctionPainter") as FunctionPainter)
                    .isVisible = e?.stateChange == 1;
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
                (mainPanel.getPainter("DerivativeFunctionPainter") as FunctionPainter)
                    .isVisible = e?.stateChange == 1;
                mainPanel.repaint();
            }
        })
    }

    private fun SetupPainters() {

        this.mainPanel.addPainter(AxisPainter);

        this.mainPanel.addPainter(FunctionPainter().also {
            it.FunctionColor = funcColor;
            it.name = "MainFunctionPainter"
        });

        this.mainPanel.addPainter(
            FunctionPainter().also {
                it.FunctionColor = derivColor;
                it.name = "DerivativeFunctionPainter"
            }
        )

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
                            (mainPanel.getPainter("MainFunctionPainter") as FunctionPainter?)?.FunctionColor = it;
                            mainPanel.repaint();
                        };
                        if (state == "point") {
                            pointColor = it;
                            PointPainter.PointColor = it; // todo: do we need to store two vars?
                            mainPanel.repaint();
                        };
                        if (state == "deriv") {
                            derivColor = it
                            (mainPanel.getPainter("DerivativeFunctionPainter") as FunctionPainter?)?.FunctionColor = it;
                        };

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
                .addGap(10)
                .addComponent(clearBtn, 20, SHRINK, SHRINK)
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

                .addComponent(clearBtn, 20, SHRINK, SHRINK)
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