package com.showdow.binkes.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.disposableRef
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.icons.fa.FaCopy
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.cssRule
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobwebx.markdown.LocalMarkdownContext
import com.zitos.web.binkes.models.ThemeByKizito
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.*



@Page
@Composable
fun HomePage() {




    var markdownText by remember { mutableStateOf("") }



    Column(Modifier.width(100.vw).height(100.vh).overflow(Overflow.Hidden)) {


        Row(Modifier.fillMaxSize().padding(16.px).gap(16.px).overflow(Overflow.Hidden)) {


            Column(
                Modifier
                    .fillMaxWidth(50.percent)
                    .fillMaxHeight()
                    .overflow(Overflow.Hidden)
                    .outline(width = 0.px, style = LineStyle.Solid, color = ThemeByKizito.PersianOrange.rgb)
            ) {
                TextArea(
                    value = markdownText,
                    attrs = Modifier
                        .fillMaxSize()
                        .padding(16.px)
                       // .fontFamily("monospace")
                        .fontSize(14.px)
                        .resize(Resize.None) // Disable resizing
                        .toAttrs {

                            onInput { event ->
                                markdownText = event.value
                            }

                            placeholder("Enter Markdown")

                        }
                )


            }



            Column(
                Modifier
                    .fillMaxWidth(50.percent)
                    .fillMaxHeight()
                    .outline(width = 3.px, style = LineStyle.Solid, color = ThemeByKizito.PersianOrange.rgb)
                   .overflow(Overflow.Hidden)
            ) {


                MarkdownRendererWithOptions(editText = markdownText

                    /*
                    "Hello  \n" +
                        "\n" +
                        "World\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "This is Showdown.js.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "It supports multiple newlines!\n" +
                        "Newlines should remain as they arennnnnn.\n"

                     */

                )

            }

        }



    }
}



// Top-level declarations
@JsName("showdown")
external object Showdown {
    class Converter(options: dynamic) {
        fun makeHtml(markdown: String): String
    }
}



@JsName("hljs")
external object HlJs {
    fun highlightElement(element: dynamic)
    fun highlightBlock(block: dynamic)
    fun highlight(language: String, code: String): dynamic
}

@Composable
fun MarkdownRendererWithOptions(editText: String) {



    var output by remember { mutableStateOf("") }

    var updateUi  by remember { mutableStateOf(true) }

    LaunchedEffect(editText) {
        // Wait briefly to ensure Showdown is loaded
        delay(100)




        // Create an options object dynamically
        val options = js("{}")
        options.strikethrough = true
        options.emoji = true
        options.tasklists = true
        options.ghCodeBlocks = true
        options.simplifiedAutoLink = true
        options.tasklists = true
        options.underline = true
        options.ellipsis = true
        options.simpleLineBreaks = true // (false) enable multiple line breaks
        options.literalMidLists = true  // Prevents merging lines inside lists
        options.smoothPreview = true
        options.parseBlockquotes = false    // Fix blockquotes
        // Advanced Formatting
        options.openLinksInNewWindow = true
        options.noHeaderId = true  //[default false] Disable the automatic generation of header ids. Setting to true overrides prefixHeaderId
        options.tables = true
        options.tablesHeaderId = true // If enabled adds an id property to table headers tags
        options.ghCompatibleHeaderId = true // Helps with GitHub-style markdown
        options.encodeEmails = false
        options.parseImgDimension = true

        options.ghMentions = true
        //By default, mentions link to https://github.com/{username}.
        // However, you can customize this behavior using ghMentionsLink.
        options.ghMentionsLink = "http://mysite.com/{u}/profile"

       // options.requireSpaceBeforeHeadingText = false // Allows headers without space after #



        //options.literalMidLists = true  // Prevents wrapping list item











        val converter = Showdown.Converter(options = options)

        output = converter.makeHtml(editText)  //Convert user input

        console.log("Input :", editText)
        console.log("Output :", output)



        updateUi = false
        delay(100)
        updateUi = true


    }

    Column(Modifier
        .fillMaxWidth()
        .padding(16.px)
        .overflow(Overflow.Scroll)
    ) {
        when {
            output.isEmpty() -> Text("Loading...")
            else ->{}
        }


        if (updateUi) {
            HtmlRenderer(output)
        }

    }




    LaunchedEffect(editText) {
        delay(1000) // Ensure the DOM is loaded before highlighting
        val codeBlocks = document.querySelectorAll("pre code").asList()

        codeBlocks.forEach { block ->
            HlJs.highlightElement(block)
        }
    }






}






@Composable
fun HtmlRenderer(html: String) {


    Div(
        attrs = showdownStyle.toModifier()
           // .textOverflow(TextOverflow.Initial)
            .overflow(Overflow.Scroll)
            .whiteSpace(WhiteSpace.Normal)
           // .overflowWrap(OverflowWrap.BreakWord)
            .wordBreak(WordBreak.KeepAll)
            .fillMaxWidth()
           // .display(DisplayStyle("-webkit-box"))

            .toAttrs {
            ref { element ->

                element.innerHTML = html

                //  element.innerHTML = html.replace(" ", "&nbsp;") // this will enable multi spacing in text



                val preBlocks = (element as? HTMLElement)?.querySelectorAll("pre")?.asList() ?: emptyList()

                preBlocks.forEach { preBlock ->

                    val codeElement =
                        (preBlock as? HTMLElement)?.querySelector("code") as? HTMLElement ?: return@forEach
                    val codeText = codeElement.textContent ?: ""


                    //  Extract language properly
                    val language = codeElement.className
                        .split(" ")
                        .find { it.startsWith("language-") }
                        ?.removePrefix("language-")
                        ?: "Plain Text"


                    // Inject the composable toolbar
                    preBlock.insertAdjacentHTML("afterbegin", "<div id='toolbar-${preBlocks.indexOf(preBlock)}'></div>")

                    val toolbarContainer = document.getElementById("toolbar-${preBlocks.indexOf(preBlock)}")

                    if (toolbarContainer != null) {
                        renderComposable(toolbarContainer) {
                            Row(
                                modifier = Modifier
                                    .maxWidth(50.percent)
                                    .maxHeight(500.px)
                                    .backgroundColor(Color.aliceblue)
                                    .borderRadius(
                                        topRight = 8.px,
                                        topLeft = 8.px,

                                        )
                                    .padding(4.px),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                P(
                                    attrs = Modifier
                                        .margin(all = 5.px)
                                        .toAttrs()
                                ) {
                                    Text(language)
                                }

                                FaCopy(
                                    modifier = Modifier
                                        .margin(all = 5.px)
                                        .onClick {
                                            copyCode(
                                                code = codeText,
                                                onCopySuccess = {
                                                    showToast("✅ Code copied!")
                                                },
                                                onCopyError = {
                                                    showToast("❌ Failed to copy!")
                                                }
                                            )
                                        },
                                    size = IconSize.LG
                                )
                            }
                        }
                    }
                }


                val blockquotes = (element as? HTMLElement)?.querySelectorAll("blockquote")?.asList() ?: emptyList()

                var topLevelIndex = 0  // Resets for every new top-level blockquote

                blockquotes.forEach { blockquote ->
                    val parent = blockquote.parentElement

                    // Detect if it's a top-level blockquote (i.e., not inside another blockquote)
                    val isTopLevel = parent?.tagName != "BLOCKQUOTE"
                    if (isTopLevel) {
                        topLevelIndex = 0 // Reset color index when a new top-level blockquote starts
                    }

                    val bgColor = "rgb(${48 + (topLevelIndex * 10)}, ${50 + (topLevelIndex * 10)}, ${54 + (topLevelIndex * 10)})"
                    val borderLeftColor = "rgb(${85 }, ${102 }, ${140 })"
                    val textColor = "rgb(${188 }, ${190 }, ${195 })"

                    (blockquote as? HTMLElement)?.let { blockquoteElement ->
                        with(blockquoteElement.style) {
                            setProperty("background-color", bgColor)
                            setProperty("border-left", "4px solid $borderLeftColor")
                            setProperty("padding", "10px")
                            setProperty("margin", "10px 20px")
                            setProperty("color", textColor)
                            setProperty("max-width", "60%") // Set max width (adjust as needed)
                            setProperty("width", "90%") // Ensures responsiveness
                        }
                    }

                    // Increase brightness for nested blockquotes within the same top-level group
                    topLevelIndex++
                }



                onDispose {
                    element.innerHTML = ""
                }
            }
        }

    )
}




val showdownStyle = CssStyle {

    // Table Borders
    cssRule("table") {
        Modifier
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Color.red
            )
            .borderCollapse(BorderCollapse.Collapse)  // Ensures clean table lines to add Borders for Table Header & Cells
    }

    //  Borders for Table Header & Cells
    cssRule("th, td") {
        Modifier
            .backgroundColor(Color.white)
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Color.black
            )
            .alignContent(com.varabyte.kobweb.compose.css.AlignContent.Center)
            .textAlign(TextAlign.Center)
            .padding(8.px)
    }

    // Header Background & Styling
    cssRule("th") {
        Modifier
            .backgroundColor(ThemeByKizito.PersianOrange.rgb)
            .fontWeight(FontWeight.Bold)
    }

    // Row Borders (Optional: Improves Table Readability)
    cssRule("tr") {
        Modifier
            .borderBottom(
                width = 1.px,
                style = LineStyle.Solid,
                color = Color.red
            )
    }

    //  Code Block Styling
    cssRule("pre code") {
        Modifier
            // .backgroundColor(Color.yellow)  // Dark background
            // .color(Color.white)  // Light text color
            .padding(4.px)
            .borderRadius(bottomLeft = 8.px, bottomRight = 8.px)
            .fontFamily("monospace")
            .maxHeight(500.px)
            .fillMaxWidth(50.percent)
            .fontSize(FontSize.Medium)
            //.outline(style = LineStyle.Groove)
    }


    // Inline code (code) → Prevents wrapping, keeps it in a single line
    cssRule("code:not(pre code)") {  // Inline `code` (but not inside <pre>)
        Modifier
            .display(DisplayStyle.InlineBlock)  // Prevents unwanted line breaks
            .backgroundColor(Color.black)
            .color(Color.orange)
            .padding(2.px, 4.px)
            .borderRadius(4.px)
            .fontFamily("monospace")
            .fontSize(FontSize.Small)
            .whiteSpace(WhiteSpace.NoWrap)  //  Prevents line breaks inside inline code
    }


/*

    cssRule("p") {
        Modifier.margin(bottom = 6.px)  // Adds spacing between paragraphs

    }
    cssRule("br") {
        Modifier
            .display(DisplayStyle.Block) // Ensures <br> breaks the line properly
    }


 */




    /*
    // inline code Styling
    cssRule("code") {
        Modifier
            .backgroundColor(Color.black)   // Dark background
            .color(Color.orange)            // Text color
            .padding(2.px, 4.px)            // Add padding for readability
            .borderRadius(4.px)             // Rounded corners
            .fontFamily("monospace")        // Use monospace font
            .fontSize(FontSize.Small)       // Smaller text size
    }

     */




    /*

    // Lists (ul, ol) → Proper indentation, no extra <p> inside <li>
     // Showdown options → Fixes rendering inconsistencies
    cssRule("ul, ol") {
        Modifier
            .padding(0.px)
            .margin(0.px, 0.px, 0.px, 20.px) // Indent for readability
    }
    cssRule("li") {
        Modifier
            .padding(4.px, 0.px)
            .lineHeight(1.5)  // Improve readability
    }
    cssRule("li p") {
        Modifier
            .margin(0.px)  // Prevent Showdown from adding <p> inside <li>
    }

     */



}





/*
val showdownStyle = CssStyle {

    cssRule("table") {
        Modifier
            .border(
                width = 0.2.px,
                style = LineStyle.Solid,
                color = Color.red
            )
    }

    cssRule( "th, td"){
        Modifier .backgroundColor(ThemeByKizito.PersianOrange.rgb)
            .border(
                width = 0.2.px,
                style = LineStyle.Solid,
                color = Color.red
            )
            .alignContent(com.varabyte.kobweb.compose.css.AlignContent.Center)
            .textAlign(TextAlign.Center)
    }

    cssRule( "th") {
        Modifier.backgroundColor(Color.lightgray)
    }


    // Code Block Styling
    cssRule("pre code") {  // Fenced code block
        Modifier
           // .backgroundColor(Color.yellow)  // Dark background
           // .color(Color.white)  // Light text color
            .padding(4.px)
            .borderRadius(bottomLeft = 8.px, bottomRight = 8.px)
            .fontFamily("monospace")

            .maxHeight(500.px)
            .fillMaxWidth(50.percent)
            .fontSize(FontSize.Medium)
            //.outline(style = LineStyle.Groove)
    }




}

 */




fun copyCode(
    code: String,
    onCopySuccess: () -> Unit,
    onCopyError: () -> Unit
) {
    val textarea = document.createElement("textarea") as org.w3c.dom.HTMLTextAreaElement
    textarea.value = code
    document.body?.appendChild(textarea)
    textarea.select()

    val success = document.execCommand("copy")
    document.body?.removeChild(textarea)

    if (success) {
        // window.alert("Code copied to clipboard!")
        onCopySuccess()

    } else {
        // console.error("Failed to copy code")
        onCopyError()
    }
}


fun showToast(message: String) {
    val toast = document.createElement("div") as org.w3c.dom.HTMLDivElement
    toast.textContent = message
    toast.style.apply {
        position = "fixed"
        bottom = "20px"
        left = "50%"
        transform = "translateX(-50%)"
        backgroundColor = "white"
        color = "black"
        padding = "10px"
        borderRadius = "5px"
        zIndex = "1000"
    }

    document.body?.appendChild(toast)

    window.setTimeout({
        document.body?.removeChild(toast)
    }, 1000) // Toast disappears after 2 seconds
}




