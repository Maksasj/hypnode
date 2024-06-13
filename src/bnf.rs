#[derive(Debug, PartialEq, Clone)]
pub enum GrammaSymbols<T> {
    NonTerminal(String),
    Terminal(T),
    Sigma
}

impl<T> GrammaSymbols<T> {
    pub fn get_string(&self) -> &str {
        match self {
            GrammaSymbols::<T>::NonTerminal(s) => s,
            GrammaSymbols::<T>::Terminal(_) => "Terminal",
            GrammaSymbols::<T>::Sigma => "Sigma",
        }
    }
}

#[derive(Debug, Clone)]
pub struct Production<T> {
    pub left: GrammaSymbols<T>,
    pub right: ( Vec<GrammaSymbols<T>>, Vec<GrammaSymbols<T>> ),
}

macro_rules! sigma {
    () => {
        GrammaSymbols::Sigma
    };
}

macro_rules! sym {
    ($label:literal) => {
        GrammaSymbols::NonTerminal($label.to_string())
    };
    ($terminal:expr) => {
        GrammaSymbols::Terminal($terminal)
    };
}

macro_rules! prod_right {
    ($($item:expr),*) => {
        vec![ 
            $( sym!($item), )*
        ]
    };
}

macro_rules! prod {
    ($left:literal => ($($right:expr),*)) => {
        Production { 
            left: sym!($left),
            right:  vec![ prod_right!( $( $right ),* ) ]
        }
    };
}

macro_rules! make_productions {
    ( $( ($left:literal => ($($right:expr),*)) ),* ) => {
        vec![
            $(
                prod!($left => ($($right),*))
            ),*
        ]
    };
}