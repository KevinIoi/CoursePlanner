/**
 * Planner Class. The main GUI used for the plan of study program
 * 
 * @author Kevin ioi - 0848838
 * @version Nov 2018
 */

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;


public class Planner extends JFrame
{
  private Controller c;
  private AdminController ac;
  
  public Planner()
  {
    super("Planner");
    ac = new AdminController();
    c = new Controller();
    welcomeMenu();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  /**
   * Sets current viewable panel to the welcome menu
   * removes all components previous contained in the
   * primary container
   * 
   */
  public void welcomeMenu()
  {
    setSize(500, 250);//set desired size for this menu
    JPanel thePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 25));//primary container for menu

    //layout baselevel framework of panel
    JLabel banner = new JLabel("Welcome to the Plan of Study Program");
    banner.setFont(new Font(null, 0, 24));
    JPanel buttons = new JPanel(new GridLayout(2,2));
    JLabel message = new JLabel("Please select an option bellow:");

    //create buttons and add listeners
      //open login dialog when button clicked
    JButton loginButton = new JButton("Login to Existing Account");
    loginButton.addActionListener(click->{openLoginDialog(false);});
      //open login dialog when button clicked    
    JButton createButton = new JButton("Create a new Account");
    createButton.addActionListener(click->{openLoginDialog(true);}); 
      //open admin window when button clicked   
    JButton adminButton = new JButton("Access Admin Commands"); 
    adminButton.addActionListener(click->{openAdminOptions();});
      //close system when button is clicked
    JButton exitButton = new JButton("Exit Program"); 
    exitButton.addActionListener(click->{System.exit(0);}); 

    //add button components to button panel
    buttons.add(loginButton);
    buttons.add(createButton);
    buttons.add(adminButton);
    buttons.add(exitButton);

    //add panels containing components to main display container
    thePanel.add(banner);
    thePanel.add(message);
    thePanel.add(buttons);

    getContentPane().removeAll();//clear frame
    add(thePanel);//replace with the welcome menu
  }

  /**
   * Creates popup JDialog for the admin options.
   * Prevents usage of other frames while admin box is up.
   * Allows access to admin server commands for the system
   * 
   */
  public void openAdminOptions()
  {
    //initilize the popup window
    JDialog adminWindow = new JDialog(this, "Administrative View");
    adminWindow.setLayout(new BorderLayout());
    adminWindow.getParent().setEnabled(false);
    adminWindow.setSize(600,500);
    adminWindow.addWindowListener(new ActivateParentOnClose(adminWindow));

    //initialize the primary components
    JPanel buttonPanel = new JPanel(new BorderLayout(2,1));
    JPanel viewPanel = new JPanel(new BorderLayout());
    JTextArea view = new JTextArea();
    view.setLineWrap(true);
    view.setBorder(BorderFactory.createLineBorder(Color.black));
    view.setEditable(false);
    JScrollPane scroll = new JScrollPane (view);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    viewPanel.add(scroll, BorderLayout.CENTER);

    //create and add buttons
    JPanel buttons = new JPanel(new GridLayout(3,2));
    JButton add = new JButton("Add Course");
    JButton remove = new JButton("Remove Course");
    JButton addDeg = new JButton("Add a Degree");
    JButton removeDeg = new JButton("Remove Degree");
    JButton list = new JButton("See all Courses");
    JButton listDeg = new JButton("See all Degreess");

    JButton exit = new JButton("Exit");
    buttons.add(add);
    buttons.add(remove);
    buttons.add(addDeg);
    buttons.add(removeDeg);
    buttons.add(list);
    buttons.add(listDeg);
    buttonPanel.add(buttons, BorderLayout.CENTER);
    buttonPanel.add(exit, BorderLayout.SOUTH);

    //button listener for adding a course
    add.addActionListener(adminAdd ->{
      //initializing new DialogWindow
      JDialog courseDetails = new JDialog(adminWindow, "Add Course");
      courseDetails.getParent().setEnabled(false);
      courseDetails.setLayout(new BorderLayout());
      courseDetails.setSize(400,250);
      courseDetails.addWindowListener(new ActivateParentOnClose(courseDetails));

      //declaring base level panels
      JPanel submitButtons = new JPanel(new GridLayout(1,2));
      JPanel courseInformation = new JPanel(new GridLayout(5,2));

      //declare and add input labels/interfaces to input panel
      JTextField cCode = new JTextField();
      JTextField cName = new JTextField();
      String credits[] = {"0.5", "0.75", "1.0"};
      JComboBox<String> cCredit = new JComboBox<String>(credits);
      String semesters[] = {"F","W","B"};
      JComboBox<String> cSemester = new JComboBox<String>(semesters);
      JTextField cPreReqs = new JTextField();
      courseInformation.add(new JLabel("Course Code (eg CIS*1500)"));
      courseInformation.add(cCode);
      courseInformation.add(new JLabel("Course Name"));
      courseInformation.add(cName);
      courseInformation.add(new JLabel("Credit"));
      courseInformation.add(cCredit);
      courseInformation.add(new JLabel("Semester Offered"));
      courseInformation.add(cSemester);
      JPanel preReqLabels = new JPanel(new GridLayout(2,1));
      preReqLabels.add(new JLabel("Prerequisites, colon delimited"));
      preReqLabels.add(new JLabel("(eg CIS*1500:CIS*2500:CIS*2750)"));
      courseInformation.add(preReqLabels);
      courseInformation.add(cPreReqs);

      //declare buttons and listeners, add to button panel
      JButton submitCourse = new JButton("Submit");
      JButton cancelCourse = new JButton("Cancel");
      submitCourse.setSize(100,100);
      cancelCourse.setSize(100,100);
      submitButtons.add(submitCourse);
      submitButtons.add(cancelCourse);

      //add action event to the submit button
      submitCourse.addActionListener( submitCourseA->{
        if(cCode.getText().trim().isEmpty() || cName.getText().trim().isEmpty()){//make sure form is complete
          new InvalidInput(courseDetails, "Error: Empty Fields, please complete form");
        }
        else//add course to database
        {
          ac.addCourse(cCode.getText().trim(), credits[cCredit.getSelectedIndex()], cName.getText().trim(), semesters[cSemester.getSelectedIndex()], cPreReqs.getText().trim());

          //update feedback panel with added information
          view.setText(cCode.getText().trim() +","+ credits[cCredit.getSelectedIndex()] +","+ cName.getText().trim() +","+ semesters[cSemester.getSelectedIndex()] +","+ cPreReqs.getText().trim());
          view.setText("Course added to Database:" + System.getProperty("line.separator") + view.getText());

          courseDetails.setVisible(false);
          courseDetails.dispose();
          courseDetails.getParent().setEnabled(true);
          adminWindow.toFront();
        }
      });

      //add action event to canel button
      cancelCourse.addActionListener(exitCourse ->{
        //close dialog, activate parent frame
        courseDetails.setVisible(false);
        courseDetails.dispose();
        courseDetails.getParent().setEnabled(true);
        adminWindow.toFront();
      });

      //add components to primary container
      courseDetails.add(courseInformation, BorderLayout.NORTH);
      courseDetails.add(submitButtons, BorderLayout.SOUTH);
      courseDetails.setVisible(true);
    });

    //button listener for removing a course
    remove.addActionListener(adminRemove->{
      //initializing new DialogWindow
      JDialog courseDetails = new JDialog(adminWindow, "Remove Course");
      courseDetails.getParent().setEnabled(false);
      courseDetails.setLayout(new BorderLayout());
      courseDetails.setSize(400,150);
      courseDetails.addWindowListener(new ActivateParentOnClose(courseDetails));
      
      //getting course information
      ArrayList<String> courses = ac.getAllCourses();
      
      //declaring base level panels
      JPanel submitButtons = new JPanel(new GridLayout(1,2));
      JPanel courseInformation = new JPanel(new GridLayout(2,1));

      //declare and add input labels/interfaces to input panel
      JComboBox<String> cCode = new JComboBox<String>();
      String splitCourse[];
      
      if(courses.isEmpty())
      {
        cCode.addItem("Database is empty...");
      }
      else
      {
        for (String str : courses) {
          splitCourse = str.split(",");
          cCode.addItem(splitCourse[0]);
        }
      }
      courseInformation.add(new JLabel("Please select a Course to remove"));
      courseInformation.add(cCode);

      //declare buttons and listeners, add to button panel
      JButton submitCourse = new JButton("Submit");
      JButton cancelCourse = new JButton("Cancel");
      submitCourse.setSize(100,100);
      cancelCourse.setSize(100,100);
      submitButtons.add(submitCourse);
      submitButtons.add(cancelCourse);

      //action event for submitting a course to remove from database
      submitCourse.addActionListener( submitCourseA->{
        if(String.valueOf(cCode.getSelectedItem()).equals("Database is empty...")){
          new InvalidInput(courseDetails, "Error: please select a valid course");
        }
        else//removing course from database
        {
          ac.deleteCourse(String.valueOf(cCode.getSelectedItem()));

          view.setText("Course removed from Database:" + System.getProperty("line.separator") + cCode.getSelectedItem().toString());

          courseDetails.setVisible(false);
          courseDetails.dispose();
          courseDetails.getParent().setEnabled(true);
          adminWindow.toFront();
        }
      });

      //caneling remove dialog
      cancelCourse.addActionListener(exitCourse ->{
        //close dialog, activate parent frame
        courseDetails.setVisible(false);
        courseDetails.dispose();
        courseDetails.getParent().setEnabled(true);
        adminWindow.toFront();
      });

      //add components to primary container
      courseDetails.add(courseInformation, BorderLayout.NORTH);
      courseDetails.add(submitButtons, BorderLayout.SOUTH);
      courseDetails.setVisible(true);
    });

    //button listener for viewing all courses
    list.addActionListener(adminList->{
      int i = 0;  
      view.setText("");

      ArrayList<String> courses = ac.getAllCourses();
      for (String str : courses) {
        i++;
        view.setText(view.getText() + System.getProperty("line.separator") + i + " - " + str ); 
      }

      if(i == 0)
        view.setText("There are no courses in the dataBase"); 
      else
        view.setText("There are " + i + " courses in the system" + System.getProperty("line.separator") + "***************************************" + System.getProperty("line.separator") + view.getText());         

      view.setCaretPosition(0);
    });

    //button listener for adding a degree
    addDeg.addActionListener(adminAddDeg->{
      //initializing new DialogWindow
      JDialog degDetails = new JDialog(adminWindow, "Add Degree");
      degDetails.getParent().setEnabled(false);
      degDetails.setLayout(new BorderLayout());
      degDetails.setSize(300,150);
      degDetails.addWindowListener(new ActivateParentOnClose(degDetails));
      
      //declaring base level panels
      JPanel submitButtons = new JPanel(new GridLayout(1,2));
      JPanel deginfoPanel = new JPanel(new GridLayout(3,2));      
      deginfoPanel.add(new JLabel("Degree Name:"));
      deginfoPanel.add(new JTextField());
      deginfoPanel.add(new JLabel("Required Courses:"));
      deginfoPanel.add(new JTextField());
      deginfoPanel.add(new JLabel("Required Credits"));
      JComboBox<Double> test = new JComboBox<Double>();
      for(Double i = 0.0; i<30.0; i+=0.5){test.addItem(i);}
      deginfoPanel.add(test);

      //declare buttons and listeners, add to button panel
      JButton submitDeg = new JButton("Submit");
      JButton cancelDeg = new JButton("Cancel");
      submitDeg.setSize(100,100);
      cancelDeg.setSize(100,100);
      submitButtons.add(submitDeg);
      submitButtons.add(cancelDeg);

      submitDeg.addActionListener(click->{
        view.setText("Degree Added");
        //close dialog, activate parent frame
        degDetails.setVisible(false);
        degDetails.dispose();
        degDetails.getParent().setEnabled(true);
        adminWindow.toFront();
      });

      cancelDeg.addActionListener(click->{
        //close dialog, activate parent frame
        degDetails.setVisible(false);
        degDetails.dispose();
        degDetails.getParent().setEnabled(true);
        adminWindow.toFront();});

        degDetails.add(deginfoPanel, BorderLayout.CENTER);
        degDetails.add(submitButtons, BorderLayout.SOUTH);
        degDetails.setVisible(true);
    });

    removeDeg.addActionListener(d ->{
      JDialog degDetails = new JDialog(adminWindow, "Remove Degree");
      degDetails.getParent().setEnabled(false);
      degDetails.setLayout(new BorderLayout());
      degDetails.setSize(300,150);
      degDetails.addWindowListener(new ActivateParentOnClose(degDetails));

      //declare buttons and listeners, add to button panel
      JPanel submitButtons = new JPanel();
      JButton submitDeg = new JButton("Submit");
      JButton cancelDeg = new JButton("Cancel");
      submitDeg.setSize(100,100);
      cancelDeg.setSize(100,100);
      submitButtons.add(submitDeg);
      submitButtons.add(cancelDeg);

      submitDeg.addActionListener(click->{
        view.setText("Degree Removed");
        //close dialog, activate parent frame
        degDetails.setVisible(false);
        degDetails.dispose();
        degDetails.getParent().setEnabled(true);
        adminWindow.toFront();
      });

      cancelDeg.addActionListener(click->{
        //close dialog, activate parent frame
        degDetails.setVisible(false);
        degDetails.dispose();
        degDetails.getParent().setEnabled(true);
        adminWindow.toFront();
      });

      JComboBox<String> degSelection = new JComboBox<String>();
      degSelection.addItem("Honours Degree - SEng");
      degSelection.addItem("Honours Degree - CS");
      degSelection.addItem("General Degree - BCG");
      
      degDetails.add(new JLabel("Select a degree to remove"), BorderLayout.NORTH);
      degDetails.add(degSelection, BorderLayout.CENTER);
      degDetails.add(submitButtons, BorderLayout.SOUTH);
      degDetails.setVisible(true);

    });

    listDeg.addActionListener(seede->{
      view.setText("Degrees:" + System.getProperty("line.separator") + "Honours Degree - SEng, CS" + System.getProperty("line.separator") + "General Degree - BCG");
      view.setCaretPosition(0);      
    });

    //exit button commands
    exit.addActionListener(exitAdmin ->{
      //close dialog, activate parent frame
      adminWindow.setVisible(false);
      adminWindow.dispose();
      adminWindow.getParent().setEnabled(true);
      toFront();
    });

    //add all components to the adminWindow container 
    adminWindow.add(buttonPanel, BorderLayout.WEST);
    adminWindow.add(viewPanel, BorderLayout.CENTER);
    adminWindow.setVisible(true);
  }

  /**
   * Brings up dialog window to log in to an account
   * 
   * @param createNew true if the login is also creating a new account, false if standard login
   */
  public void openLoginDialog(boolean createNew)
  {
    //Set up dialog window
    JDialog loginDialog = new JDialog(this, "Account Menu");
    loginDialog.setLayout(new BorderLayout());
    loginDialog.setSize(400,150);
    loginDialog.getParent().setEnabled(false);
    //safety measure to ensure parent frame is activated when dialog is closed
    loginDialog.addWindowListener(new ActivateParentOnClose(loginDialog));
    
    //initialize String input panel
    JPanel loginInput = new JPanel(new GridLayout(3,1));//main String input container
    JPanel studID = new JPanel(new GridLayout(1,2));
    JPanel studFname = new JPanel(new GridLayout(1,2));//because we need the student name to query students for some reason
    JPanel studLName = new JPanel(new GridLayout(1,2));
    studID.add(new JLabel("Student ID:"));
    studFname.add(new JLabel("First Name:"));
    studLName.add(new JLabel("Last Name:"));
    JTextField studIdInput = new JTextField();
    JTextField studFnameInput  = new JTextField();
    JTextField studLnameInput = new JTextField();
    studID.add(studIdInput);
    studFname.add(studFnameInput);
    studLName.add(studLnameInput);
    loginInput.add(studID);
    loginInput.add(studFname);
    loginInput.add(studLName);
    
    //initialize button submit panel
    JPanel loginButtons = new JPanel(new GridLayout(1,2));
    JButton loginSubmit = new JButton();
    JButton loginCancel = new JButton("Cancel");
    loginButtons.add(loginSubmit);
    loginButtons.add(loginCancel);
    
    //close dialog box if user cancels login
    loginCancel.addActionListener(cancelLogin ->{
      loginDialog.setVisible(false);
      loginDialog.dispose();
      loginDialog.getParent().setEnabled(true);
      toFront();
    });
    
    if(createNew)//if the user is creating a new account
    {
      //set the action event for the submit button to follow the create account path
      loginSubmit.setText("Create Plan");
      loginSubmit.addActionListener(createAcct -> {
        if (studIdInput.getText().trim().isEmpty() || studFnameInput.getText().trim().isEmpty() || studLnameInput.getText().trim().isEmpty())//make sure form is complete
        {
          new InvalidInput(loginDialog, "Error: Incomplete form, please fillout all boxes");  
        }
        else//close this dialog and follow the create account dialog
        {
         try {
           Integer.parseInt(studIdInput.getText().trim());
           loginDialog.setVisible(false);
           loginDialog.dispose();
           loginDialog.getParent().setEnabled(true);
           toFront();
           c.createStudent(studIdInput.getText().trim(), studFnameInput.getText().trim(), studLnameInput.getText().trim());
           createAccountDialog(this);
          }catch(NumberFormatException nfe){
            new InvalidInput(loginDialog, "ERROR: Invalid student ID, please enter a number");
          }
        }        
      });
    }
    else//user is logging into a previously made account
    {
      loginSubmit.setText("Login");
      
      loginSubmit.addActionListener(loginAcct -> {
        if (studIdInput.getText().trim().isEmpty() || studFnameInput.getText().trim().isEmpty() || studLnameInput.getText().trim().isEmpty())//make sure form is complete
        {
          new InvalidInput(loginDialog, "Error: Incomplete form, please fillout all boxes");  
        }
        else
        {          
          try {
            c.loadStudent(studFnameInput.getText().trim(),  studLnameInput.getText().trim(), studIdInput.getText());
            loginDialog.setVisible(false);
            loginDialog.dispose();
            loginDialog.getParent().setEnabled(true);
            toFront();
            mainUserPage();
          } catch (StudentNotFoundException e) {
            new InvalidInput(loginDialog, "Error: There is no student record under that information");  
          }
        }
      });
    }
    
    //add base level components to the main dialog frame
    loginDialog.add(loginInput,BorderLayout.CENTER);
    loginDialog.add(loginButtons,BorderLayout.SOUTH);
    loginDialog.setVisible(true);
  }
  
  public void createAccountDialog(JFrame frame)
  {
    //Set up dialog window
    JDialog loginDialog = new JDialog(frame, "Select Degree");
    loginDialog.setLayout(new BorderLayout());
    loginDialog.setSize(250,150);
    loginDialog.getParent().setEnabled(false);
    //safety measure to ensure parent frame is activated when dialog is closed
    loginDialog.addWindowListener(new ActivateParentOnClose(loginDialog));

    //initialize button submit panel
    JPanel loginButtons = new JPanel(new GridLayout(1,2));
    JButton loginSubmit = new JButton("Submit");
    JButton loginCancel = new JButton("Cancel");
    loginButtons.add(loginSubmit);
    loginButtons.add(loginCancel);

    //initialize input area
    JPanel degreeSelection = new JPanel(new GridLayout(2,2));
    JComboBox<String> degreeSelect = new JComboBox<String>();
    JComboBox<String> programSelect = new JComboBox<String>();
    degreeSelection.add(new JLabel("Degree:"));
    degreeSelection.add(degreeSelect);
    degreeSelection.add(new JLabel("Program:"));
    degreeSelection.add(programSelect);
    //setup combo box interactions
    programSelect.addItem("BCG");
    Set<String> degreeProgs = c.getDegreeOptions();
    HashSet<String> degrees = new HashSet<String>();
    //put unique degree options into a set
    for (String str : degreeProgs){degrees.add(str.split(":")[0]);}
    for (String s : degrees) { degreeSelect.addItem(s);};//add all degree options to combobox
    //update program options when degree is selected
    degreeSelect.addActionListener (choseDeg ->{
      programSelect.removeAllItems();
      Set<String> degr = c.getDegreeOptions();
      for (String st : degr){
        if((st.split(":")[0]).equals(String.valueOf(degreeSelect.getSelectedItem())))
        {
          programSelect.addItem(st.split(":")[1]);
        }
      }
    });

    //set event listeners for the buttons
    //close dialog box properly on cancel
    loginCancel.addActionListener(cncl->{
      loginDialog.setVisible(false);
      loginDialog.dispose();
      loginDialog.getParent().setEnabled(true);
      toFront();
    });

    //submit form on submit, move to main user screen
    loginSubmit.addActionListener(click->{
      //make sure all info has been provided
      if (String.valueOf(degreeSelect.getSelectedItem()).trim().isEmpty() || String.valueOf(programSelect.getSelectedItem()).trim().isEmpty()) {
        new InvalidInput(loginDialog, "ERROR: Missing information, Please complete the form");
      }
      else
      {
        try {
          c.setStudentDegree(String.valueOf(degreeSelect.getSelectedItem()).trim()+":"+String.valueOf(programSelect.getSelectedItem()).trim());
          loginDialog.setVisible(false);
          loginDialog.dispose();
          loginDialog.getParent().setEnabled(true);
          mainUserPage();
          toFront();
        } catch (DegreeNotFoundException e) {
          new InvalidInput(loginDialog, "ERROR: Invalid degree selected, I don't know how you did that");
        }
      }
    });

    //add components to main container
    loginDialog.add(degreeSelection, BorderLayout.CENTER);
    loginDialog.add(loginButtons, BorderLayout.SOUTH);
    loginDialog.setVisible(true);
  }

  public void mainUserPage()
  {
    getContentPane().removeAll();//clear frame
    setSize(800, 750);//set desired size for this menu

    JPanel mainPane = new JPanel(new GridLayout(1,2));//primary conatiner for this view

    //setup menu bar
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Account Options");
    JMenuItem save = new JMenuItem("Save plan details");
    JMenuItem changeDegs = new JMenuItem("Change Degree Programs");
    JMenuItem logout = new JMenuItem("Logout");
    //saves current student state
    save.addActionListener(sv->{
      c.saveStudent();
      new InvalidInput(this, "Account Saved");
    });
    //logout of this account, go back to welcome
    logout.addActionListener(lo->{
      c.dropStudent();
      setJMenuBar(null);
      welcomeMenu();
    });
    changeDegs.addActionListener(changedeg->{
      createAccountDialog(this);
    });
    menuBar.add(menu);
    menu.add(save);
    menu.add(changeDegs);
    menu.add(logout);
    setJMenuBar(menuBar);

    //initialize the two subpanes
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(9, 2));
    mainPane.add(buttonPanel);
    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.PAGE_AXIS));
    mainPane.add(outputPanel);

    //Banner
    JLabel header = new JLabel("Welcome to Your Plan of Study",SwingConstants.CENTER);
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
    header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    JLabel info = new JLabel(c.getStudentName() + " - " + c.getStudentDegree() ,SwingConstants.CENTER);
    info.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
    info.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    //display screen
    JTextArea console = new JTextArea();
    console.setLineWrap(true);
    console.setBorder(BorderFactory.createLineBorder(Color.black));
    console.setEditable(false);
    JScrollPane scroll = new JScrollPane (console);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    outputPanel.add(header, JPanel.CENTER_ALIGNMENT);
    outputPanel.add(info,JPanel.CENTER_ALIGNMENT);    
    outputPanel.add(scroll,JPanel.CENTER_ALIGNMENT);
    

    //button panel
    JButton viewPlan = new JButton("View Plan");
    viewPlan.setToolTipText("View all courses planned and completed, by Semester taken");
    viewPlan.setActionCommand("viewPlan");
    viewPlan.addActionListener(new PlanAction(this, console));
    buttonPanel.add(viewPlan);

    JButton updateGrade = new JButton("Update Transcript Record");
    updateGrade.setToolTipText("Update a grade for a course in your transcript");
    updateGrade.setActionCommand("updateGrade");
    updateGrade.addActionListener(new PlanAction(this, console));
    buttonPanel.add(updateGrade);

    JButton addRecord = new JButton("Add Transcript Record");
    addRecord.setToolTipText("Add a course to your transcript");
    addRecord.setActionCommand("addRecord");
    addRecord.addActionListener(new PlanAction(this, console));
    buttonPanel.add(addRecord);

    JButton removeRecord = new JButton("Remove Transcript Record");
    removeRecord.setToolTipText("Remove a course from your transcript");
    removeRecord.setActionCommand("removeRecord");
    removeRecord.addActionListener(new PlanAction(this, console));
    buttonPanel.add(removeRecord);

    JButton planCourse = new JButton("Plan a course");
    planCourse.setToolTipText("Add a course to your plan");
    planCourse.setActionCommand("planCourse");
    planCourse.addActionListener(new PlanAction(this, console));
    buttonPanel.add(planCourse);

    JButton unplanCourse = new JButton("Unplan a course");
    unplanCourse.setToolTipText("Remove a course from your plan");
    unplanCourse.setActionCommand("unplanCourse");
    unplanCourse.addActionListener(new PlanAction(this, console));
    buttonPanel.add(unplanCourse);

    JButton viewCourses = new JButton("View Available Courses");
    viewCourses.setToolTipText("Display all courses offered by the University");
    viewCourses.setActionCommand("viewCourses");
    viewCourses.addActionListener(new PlanAction(this, console));
    buttonPanel.add(viewCourses);
    
    JButton viewPreReqs = new JButton("View Course PreReqs");
    viewPreReqs.setToolTipText("View a list of Prerequisite courses for a course offered at the university");
    viewPreReqs.setActionCommand("viewPreReqs");
    viewPreReqs.addActionListener(new PlanAction(this, console));
    buttonPanel.add(viewPreReqs);
    
    JButton viewPlanPreReqs = new JButton("View Prereqs for Plan");
    viewPlanPreReqs.setToolTipText("View a list of prerequisites needed for your course plan");
    viewPlanPreReqs.setActionCommand("viewPlanPreReqs");
    viewPlanPreReqs.addActionListener(new PlanAction(this, console));
    buttonPanel.add(viewPlanPreReqs);
    
    JButton viewMissingReqs = new JButton("Missing Requirements");
    viewMissingReqs.setToolTipText("View list of required courses that are not planned or completed");
    viewMissingReqs.setActionCommand("viewMissingReqs");
    viewMissingReqs.addActionListener(new PlanAction(this, console));
    buttonPanel.add(viewMissingReqs);

    JButton viewReqsTBD = new JButton("Incomplete Requirements");
    viewReqsTBD.setToolTipText("View list of required courses not on your transcript");
    viewReqsTBD.setActionCommand("viewReqsTBD");
    viewReqsTBD.addActionListener(new PlanAction(this, console));
    buttonPanel.add(viewReqsTBD);

    JButton calcCredits = new JButton("Calculate Credit Count");
    calcCredits.setToolTipText("Calculate the number of credits you have completed");
    calcCredits.setActionCommand("calcCredits");
    calcCredits.addActionListener(new PlanAction(this, console));
    buttonPanel.add(calcCredits);    
    
    JButton calcRemCredits = new JButton("Calculate Planned Credits");
    calcRemCredits.setToolTipText("Calculate the number of credits that are planned but not completed");
    calcRemCredits.setActionCommand("calcRemCredits");
    calcRemCredits.addActionListener(new PlanAction(this, console));
    buttonPanel.add(calcRemCredits);

    JButton calcMisCredits = new JButton("Calculate Missing Credits");
    calcMisCredits.setToolTipText("Calculate the number of credits needed to be planned to complete your degree");
    calcMisCredits.setActionCommand("calcMisCredits");
    calcMisCredits.addActionListener(new PlanAction(this, console));
    buttonPanel.add(calcMisCredits);

    JButton gpa = new JButton("Check GPA");
    gpa.setToolTipText("Calculate your overall GPA");
    gpa.setActionCommand("gpa");
    gpa.addActionListener(new PlanAction(this, console));
    buttonPanel.add(gpa);
    
    JButton gpaSpecific = new JButton("Check Dept GPA");
    gpaSpecific.setToolTipText("Calculate your GPA in a specific department (eg CIS, MATH)");
    gpaSpecific.setActionCommand("gpaSpecific");
    gpaSpecific.addActionListener(new PlanAction(this, console));
    buttonPanel.add(gpaSpecific);

    JButton gpaRecent = new JButton("Check Recent GPA");
    gpaRecent.setToolTipText("Calculate your GPA for the last 10 courses you've completed");
    gpaRecent.setActionCommand("gpaRecent");
    gpaRecent.addActionListener(new PlanAction(this, console));
    buttonPanel.add(gpaRecent);

    JButton eval = new JButton("Evaluate Plan");
    eval.setToolTipText("Check if your plan satisfies your degree requirements");
    eval.setActionCommand("eval");
    eval.addActionListener(new PlanAction(this, console));
    buttonPanel.add(eval);
    
    add(mainPane);//add this panel to the program's JFrame
    validate();
  }

  private class PlanAction implements ActionListener
  {
    JFrame owner;
    JTextArea output;


    PlanAction(){}

    PlanAction(JFrame main, JTextArea display)
    {
      this.owner = main;
      this.output = display;
    }

    public void actionPerformed(ActionEvent e)
    {
      if(e.getActionCommand().equals("viewPlan"))//display courses in plan by semester 
      {
        ArrayList<String> plan = c.getCoursePlan();
        String semester = "kevin";
        String splitInfo[];

        //exit if the plan is empty
        if (plan.isEmpty()) {
          output.setText("Your plan is empty");
          return;
        }

        output.setText("");
        for (String str : plan)
        {
          splitInfo = str.split(",");
          if (!splitInfo[0].equals(semester))
          {
            semester = splitInfo[0];
            output.setText(output.getText() + System.getProperty("line.separator") + splitInfo[0]);  
          }
          output.setText(output.getText() + System.getProperty("line.separator") + splitInfo[1]);
          
          if(splitInfo.length > 2)
          {
            if(splitInfo[2].equals("-1"))
            {
              output.setText(output.getText() + " - planned");
            }
            else
            {
              output.setText(output.getText() + " - grade: " + splitInfo[2]);              
            }
          }
        }

        output.setText("Your course plan is:" + System.getProperty("line.separator") + output.getText());
      }
      else if(e.getActionCommand().equals("updateGrade"))//update the grade of a transcript attempt
      {
        //set up base
        JDialog updateDialog = new JDialog(owner, "Update Record");
        updateDialog.setSize(300,150);
        owner.setEnabled(false);
        updateDialog.addWindowListener(new ActivateParentOnClose(updateDialog));
        updateDialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        JPanel inputPanel = new JPanel(new GridLayout(2,2));

        //create components
        inputPanel.add(new JLabel("Select a Record to Update:"));
        JComboBox<String> records = new JComboBox<String>();
        inputPanel.add(records);
        inputPanel.add(new JLabel("select an updated grade"));
        JComboBox<String> grades = new JComboBox<String>();
        grades.addItem("P");
        grades.addItem("F");
        grades.addItem("INC");
        grades.addItem("MNR");
        for(int i=100; i>=0; i--){grades.addItem(String.valueOf(i));}
        inputPanel.add(grades);

        ArrayList<String> recordStr = c.getTranscript();

        if (recordStr.isEmpty()) {//exit if the transcript is empty
          new InvalidInput(owner,"There are no records to update");
          updateDialog.setVisible(false);
          updateDialog.dispose();
          return;
        }

        for (String str : recordStr){//add all records to combobox
          records.addItem(str);  
        }


        JButton submit = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");   
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        //set event listeners for the buttons
        //close dialog box properly on cancel
        cancel.addActionListener(c->{
          updateDialog.setVisible(false);
          updateDialog.dispose();
          owner.setEnabled(true);
          owner.toFront();
        });

        submit.addActionListener(s->{

          try {
            String course = String.valueOf(records.getSelectedItem()).split("-")[0];
            c.updateTranscript(course.split(" ")[0], course.split(" ")[1], String.valueOf(grades.getSelectedItem()));
            output.setText("Updated " + course + " with grade: " + String.valueOf(grades.getSelectedItem()));
            updateDialog.setVisible(false);
            updateDialog.dispose();
            owner.setEnabled(true);
            owner.toFront();
          } catch (CourseNotFoundException es) {
            new InvalidInput(updateDialog, "No matching record was not found on your transcript");
          }
        });

        updateDialog.add(inputPanel, BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);
        updateDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("addRecord"))//add a transcript record
      {
        //set up base
        JDialog addDialog = new JDialog(owner, "Add Record");
        addDialog.setSize(350,200);
        owner.setEnabled(false);
        addDialog.addWindowListener(new ActivateParentOnClose(addDialog));
        addDialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        JPanel inputPanel = new JPanel(new GridLayout(3,2));

        //create components
        inputPanel.add(new JLabel("Course Code:"));
        JTextField code = new JTextField();
        inputPanel.add(code);
        inputPanel.add(new JLabel("Semester Taken:"));
        JTextField semester = new JTextField();
        inputPanel.add(semester);
        inputPanel.add(new JLabel("Grade recieved"));
        JComboBox<String> grades = new JComboBox<String>();
        grades.addItem("P");
        grades.addItem("F");
        grades.addItem("INC");
        grades.addItem("MNR");
        for(int i=100; i>=0; i--){grades.addItem(String.valueOf(i));}
        inputPanel.add(grades);

        JButton submit = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");   
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        //set event listeners for the buttons
        //close dialog box properly on cancel
        cancel.addActionListener(c->{
          addDialog.setVisible(false);
          addDialog.dispose();
          owner.setEnabled(true);
          owner.toFront();
        });

        submit.addActionListener(s->{
          try {
            c.completeCourse(code.getText().trim(), semester.getText().trim(), String.valueOf(grades.getSelectedItem()));
            output.setText("Added record " + code.getText().trim() + " taken in: " + semester.getText().trim()+ " ,with grade: " + String.valueOf(grades.getSelectedItem()));
            addDialog.setVisible(false);
            addDialog.dispose();
            owner.setEnabled(true);
            owner.toFront();
          } catch (CourseNotFoundException cne) {
            new InvalidInput(addDialog, "That course code is not available, please choose an offered course");
          }
          catch (InvalidChoiceException ie)
          {
            new InvalidInput(addDialog, "The course is not offered in that semester");
          }
        });

        addDialog.add(inputPanel, BorderLayout.CENTER);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);
        addDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("removeRecord"))//remove transcript record
      {
        //set up base
        JDialog updateDialog = new JDialog(owner, "Delete Record");
        updateDialog.setSize(350,150);
        owner.setEnabled(false);
        updateDialog.addWindowListener(new ActivateParentOnClose(updateDialog));
        updateDialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        JPanel inputPanel = new JPanel(new GridLayout(1,2));

        //create components
        inputPanel.add(new JLabel("Select a Record to Delete:"));
        JComboBox<String> records = new JComboBox<String>();
        inputPanel.add(records);

        //populate records
        ArrayList<String> recordStr = c.getTranscript();
        if (recordStr.isEmpty()) {//exit if the transcript is empty
          new InvalidInput(owner,"There are no records to update");
          updateDialog.setVisible(false);
          updateDialog.dispose();
          return;
        }
        for (String str : recordStr){//add all records to combobox
          records.addItem(str);  
        }

        //create buttons
        JButton submit = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");   
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        //set event listeners for the buttons
        //close dialog box properly on cancel
        cancel.addActionListener(c->{
          updateDialog.setVisible(false);
          updateDialog.dispose();
          owner.setEnabled(true);
          owner.toFront();
        });

        //submit transcript record removal
        submit.addActionListener(s->{
          try {
            String course = String.valueOf(records.getSelectedItem()).split("-")[0];
            c.removeFromTranscript(course.split(" ")[0], course.split(" ")[1]);
            output.setText("Removed " + course + " from your transcript");
            updateDialog.setVisible(false);
            updateDialog.dispose();
            owner.setEnabled(true);
            owner.toFront();
          } catch (CourseNotFoundException es) {
            new InvalidInput(updateDialog, "No matching record was not found on your transcript");
          }
        });

        updateDialog.add(inputPanel, BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);
        updateDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("planCourse"))//plan a course
      {
         //set up base
         JDialog addDialog = new JDialog(owner, "Plan Course");
         addDialog.setSize(250,150);
         owner.setEnabled(false);
         addDialog.addWindowListener(new ActivateParentOnClose(addDialog));
         addDialog.setLayout(new BorderLayout());
 
         JPanel buttonPanel = new JPanel(new GridLayout(1,2));
         JPanel inputPanel = new JPanel(new GridLayout(2,2));
 
         //create components
         inputPanel.add(new JLabel("Course Code:"));
         JTextField code = new JTextField();
         inputPanel.add(code);
         inputPanel.add(new JLabel("Semester Planned:"));
         JTextField semester = new JTextField();
         inputPanel.add(semester);
 
         JButton submit = new JButton("Confirm");
         JButton cancel = new JButton("Cancel");   
         buttonPanel.add(submit);
         buttonPanel.add(cancel);
         //set event listeners for the buttons
         //close dialog box properly on cancel
         cancel.addActionListener(c->{
           addDialog.setVisible(false);
           addDialog.dispose();
           owner.setEnabled(true);
           owner.toFront();
         });
 
         submit.addActionListener(s->{
           try {
             c.planCourse(code.getText().trim(), semester.getText().trim());
             output.setText("Planned course" + code.getText().trim() + " for: " + semester.getText().trim());
             addDialog.setVisible(false);
             addDialog.dispose();
             owner.setEnabled(true);
             owner.toFront();
           } catch (CourseNotFoundException cne) {
             new InvalidInput(addDialog, "That course code is not available, please choose an offered course");
           }
           catch (InvalidChoiceException ie)
           {
             new InvalidInput(addDialog, "The course is not offered in that semester");
           }
         });
 
         addDialog.add(inputPanel, BorderLayout.CENTER);
         addDialog.add(buttonPanel, BorderLayout.SOUTH);
         addDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("unplanCourse"))//unplan a course
      {
        //set up base
        JDialog updateDialog = new JDialog(owner, "Remove Planned Course");
        updateDialog.setSize(350,150);
        owner.setEnabled(false);
        updateDialog.addWindowListener(new ActivateParentOnClose(updateDialog));
        updateDialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        JPanel inputPanel = new JPanel(new GridLayout(1,2));

        //create components
        inputPanel.add(new JLabel("Select a course to remove:"));
        JComboBox<String> records = new JComboBox<String>();
        inputPanel.add(records);

        //populate records
        ArrayList<String> recordStr = c.getPlannedCourses();
        if (recordStr.isEmpty()) {//exit if the transcript is empty
          new InvalidInput(owner,"There are no courses planned");
          updateDialog.setVisible(false);
          updateDialog.dispose();
          return;
        }
        for (String str : recordStr){//add all records to combobox
          records.addItem(str);  
        }

        //create buttons
        JButton submit = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");   
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        //set event listeners for the buttons
        //close dialog box properly on cancel
        cancel.addActionListener(c->{
          updateDialog.setVisible(false);
          updateDialog.dispose();
          owner.setEnabled(true);
          owner.toFront();
        });

        //submit transcript plan removal
        submit.addActionListener(s->{
          try {
            String course = String.valueOf(records.getSelectedItem());
            c.removeFromPlan(course.split(" ")[0], course.split(" ")[1]);
            output.setText("Removed " + course + " from your plan");
            updateDialog.setVisible(false);
            updateDialog.dispose();
            owner.setEnabled(true);
            owner.toFront();
          } catch (CourseNotFoundException es) {
            new InvalidInput(updateDialog, "No matching record was not found on your plan");
          }
        });

        updateDialog.add(inputPanel, BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);
        updateDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("viewCourses"))//display all courses
      {
        ArrayList<String> courses = c.getOfferedCourses();
        int i = 0;
        output.setText("");
        for (String str : courses){//loop through all offerings printing them to display
          i++;
          output.setText(output.getText() +  System.getProperty("line.separator") + String.valueOf(i) + " - " + str);
        }
        output.setText("There are " +String.valueOf(i)+ " courses offered"+System.getProperty("line.separator")+"***************************" +System.getProperty("line.separator")+output.getText());

      }
      else if(e.getActionCommand().equals("viewPreReqs"))//lookup prereqs
      {
          //set up base
          JDialog addDialog = new JDialog(owner, "View Prerequisites");
          addDialog.setSize(250,150);
          owner.setEnabled(false);
          addDialog.addWindowListener(new ActivateParentOnClose(addDialog));
          addDialog.setLayout(new BorderLayout());
  
          JPanel buttonPanel = new JPanel(new GridLayout(1,2));
          JPanel inputPanel = new JPanel(new GridLayout(1,2));
  
          //create components
          inputPanel.add(new JLabel("Course Code:"));
          JTextField code = new JTextField();
          inputPanel.add(code);
  
          JButton submit = new JButton("Confirm");
          JButton cancel = new JButton("Cancel");   
          buttonPanel.add(submit);
          buttonPanel.add(cancel);
          //set event listeners for the buttons
          //close dialog box properly on cancel
          cancel.addActionListener(c->{
            addDialog.setVisible(false);
            addDialog.dispose();
            owner.setEnabled(true);
            owner.toFront();
          });
  
          submit.addActionListener(s->{
            try {
              String reqs = c.getPreReqs(code.getText().trim());
              output.setText("The Prereqs for:"  +code.getText().trim() + " are: " + reqs);
              addDialog.setVisible(false);
              addDialog.dispose();
              owner.setEnabled(true);
              owner.toFront();
            } catch (CourseNotFoundException cne) {
              new InvalidInput(addDialog, "That course code is not available, please choose an offered course");
            }
          });
  
          addDialog.add(inputPanel, BorderLayout.CENTER);
          addDialog.add(buttonPanel, BorderLayout.SOUTH);
          addDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("viewPlanPreReqs"))
      {
        output.setText("Your planned courses with prerequisites are:");
        ArrayList<String> str = c.getAllPrereqsForPlan();
        for (String s : str) {
          output.setText(output.getText() + System.getProperty("line.separator") + s);          
        }
      }
      else if(e.getActionCommand().equals("viewMissingReqs"))
      {
        ArrayList<String> str = c.getMissingCourses();

        if(str.isEmpty())
        {
          output.setText("You have planned or taken all required courses");
        }
        else
        {
          output.setText("Your plan is missing the following required courses for your degree:");
          for (String s : str) {
            output.setText(output.getText() + System.getProperty("line.separator") + s);          
          }
        }
      }
      else if(e.getActionCommand().equals("viewReqsTBD"))
      {
        output.setText("The required courses you have planned but not complete are:");
        ArrayList<String> str = c.getIncompleteReqs();
        for (String s : str) {
          output.setText(output.getText() + System.getProperty("line.separator") + s);          
        }
      }
      else if(e.getActionCommand().equals("calcCredits"))
      {
        output.setText("You have completed " + String.valueOf(c.countCreditsEarned()) + " credits");
      }    
      else if(e.getActionCommand().equals("calcRemCredits"))
      {
        output.setText("You have planned " + String.valueOf(c.countCreditsPlanned()) + " credits");
      }
      else if(e.getActionCommand().equals("calcMisCredits"))
      {
        output.setText("You have to plan " + String.valueOf(c.creditsNeeded()) + " more credits to" + System.getProperty("line.separator") + "fulfill your degree requirements");
      }
      else if(e.getActionCommand().equals("gpa"))
      {
        try {
          output.setText("Your overall GPA is: " + String.valueOf(c.calculateGPA()));
          
        } catch (InvalidChoiceException eee) {
          new InvalidInput(owner, "You have not completed any courses");
        }
      }
      else if(e.getActionCommand().equals("gpaSpecific"))
      {
        //set up base
        JDialog addDialog = new JDialog(owner, "Calculate GPA");
        addDialog.setSize(500,100);
        owner.setEnabled(false);
        addDialog.addWindowListener(new ActivateParentOnClose(addDialog));
        addDialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        JPanel inputPanel = new JPanel(new GridLayout(1,2));

        //create components
        JPanel inputText = new JPanel(new GridLayout(2,1));
        inputText.add(new JLabel("Deptartment code to calculate"));
        inputText.add(new JLabel("GPA for. eg 'CIS' for all CIS courses)"));
        inputPanel.add(inputText);


        JTextField code = new JTextField();
        inputPanel.add(code);

        JButton submit = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");   
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        //set event listeners for the buttons
        //close dialog box properly on cancel
        cancel.addActionListener(c->{
          addDialog.setVisible(false);
          addDialog.dispose();
          owner.setEnabled(true);
          owner.toFront();
        });

        submit.addActionListener(s->{
          if(code.getText().trim().isEmpty())
          {
            new InvalidInput(addDialog, "Please enter a deptartment code");
          }
          else
          {
            try {
              output.setText("Your GPA for " + code.getText().trim() + " courses is :" + c.calculateGPA(code.getText().trim()));
              addDialog.setVisible(false);
              addDialog.dispose();
              owner.setEnabled(true);
              owner.toFront();
            }
            catch (InvalidChoiceException ie)
            {
              new InvalidInput(addDialog, "You have not taken any courses in the " + code.getText().trim() + " department");
            }
          }
        });

        addDialog.add(inputPanel, BorderLayout.CENTER);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);
        addDialog.setVisible(true);
      }
      else if(e.getActionCommand().equals("gpaRecent"))
      {
        try {
          output.setText("The GPA across your 10 most recent courses is: " + String.valueOf(c.calcRecentGPA()));
          
        } catch (InvalidChoiceException eee) {
          new InvalidInput(owner, "You have not completed 10 courses");
        }
      }
      else if(e.getActionCommand().equals("eval"))
      {
        if(c.evaluatePlan())
        {
          output.setText("Your plan satisfies graduation requirements for your degree");
        }
        else
        {
          output.setText("Your plan does not satisfy graduation requirements for your degree");
        }
      }
    }

  }


  /**
   * Window Listener used to ensure that parent windows are
   * activated on child window close
   * 
   * mostly just insurance, incase I messed up somewhere
   */
  private class ActivateParentOnClose implements WindowListener
  {
    JDialog dialog;

    /**
     * zero parameter constructor
     */
    public ActivateParentOnClose(){}

    /**
     * single parameter constructor
     * @param theWindow the parent window of the dialog box to be created
     */
    public ActivateParentOnClose(JDialog theWindow)
    {
      dialog = theWindow;
    }
    
    //required action events 
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {
      dialog.getParent().setEnabled(true);
    }
    public void windowActivated(WindowEvent e){}
    public void windowClosing(WindowEvent e) {
      dialog.getParent().setEnabled(true);
    }
  }
  
  public boolean equals(Planner obj) {
    return false;
  }
  
  public String toString() {
    return "Planner";
  }

  public static void main(String[] args) {
    new Planner();
  }
  
}
