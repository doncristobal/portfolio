package name.abuchen.portfolio.ui.dialogs.palette;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import name.abuchen.portfolio.model.Bookmark;
import name.abuchen.portfolio.ui.Images;
import name.abuchen.portfolio.ui.Messages;
import name.abuchen.portfolio.ui.dialogs.palette.CommandPalettePopup.Element;
import name.abuchen.portfolio.ui.dialogs.palette.CommandPalettePopup.ElementProvider;
import name.abuchen.portfolio.ui.selection.SecuritySelection;
import name.abuchen.portfolio.ui.selection.SelectionService;
import name.abuchen.portfolio.ui.util.DesktopAPI;

/* package */ class BookmarkElements implements ElementProvider
{
    private static class BookmarkElement implements Element
    {
        private final Bookmark bookmark;
        private final SecuritySelection selection;

        public BookmarkElement(Bookmark bookmark, SecuritySelection selection)
        {
            this.bookmark = bookmark;
            this.selection = selection;
        }

        @Override
        public String getTitel()
        {
            return bookmark.getLabel();
        }

        @Override
        public String getSubtitle()
        {
            return selection.getSecurity().getName() + " " + Messages.BookmarksListView_bookmark; //$NON-NLS-1$
        }

        @Override
        public Images getImage()
        {
            return Images.BOOKMARK;
        }

        @Override
        public void execute()
        {
            DesktopAPI.browse(bookmark.constructURL(selection.getClient(), selection.getSecurity()));
        }
    }

    @Inject
    private SelectionService selectionService;

    @Override
    public List<Element> getElements()
    {
        SecuritySelection selection = selectionService.getSelection();

        if (selection == null)
            return Collections.emptyList();

        return selection.getClient().getSettings().getBookmarks().stream() //
                        .filter(b -> !b.isSeparator()) //
                        .map(b -> new BookmarkElement(b, selection)) //
                        .collect(Collectors.toList());
    }
}
